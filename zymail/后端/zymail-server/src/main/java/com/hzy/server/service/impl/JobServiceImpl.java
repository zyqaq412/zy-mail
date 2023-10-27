package com.hzy.server.service.impl;

import com.hzy.server.config.ConfigProperties;
import com.hzy.server.constant.AppHttpCodeEnum;
import com.hzy.server.constant.SystemConstant;
import com.hzy.server.exception.SystemException;
import com.hzy.server.job.LocalSendMailJob;
import com.hzy.server.job.RemoteSendMailJob;
import com.hzy.server.model.entity.JobVo;
import com.hzy.server.model.entity.Mail;
import com.hzy.server.service.JobService;
import com.hzy.server.utils.HttpClientUtils;
import com.hzy.server.utils.IpUtils;
import com.hzy.server.utils.RedisCache;
import com.hzy.server.utils.Result;
import org.quartz.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.util.*;

/**
 * @title: JobServerImpl
 * @Author zxwyhzy
 * @Date: 2023/10/4 17:21
 * @Version 1.0
 */
@Service
public class JobServiceImpl implements JobService {
    @Autowired
    private Scheduler scheduler;

    @Autowired
    private RedisCache redisCache;

    @Override
    public Result getAllJobs() {
        // GroupMatcher<TriggerKey> groupMatcher = GroupMatcher.anyTriggerGroup();
        return getJobsByGroupMatcher(null);
        // 这样获取的效率不高 推荐使用GroupMatcher
        /*
         *
         * GroupMatcher 是 Quartz 调度框架中的一个工具类，
         * 用于在筛选触发器或作业时匹配特定的组。
         * 它的主要作用是帮助你根据不同的条件来选择特定的触发器组或作业组。
         * */
/*        try {
            // 获取所有触发器组的名称
            List<String> triggerGroupNames = scheduler.getTriggerGroupNames();
            for (String groupName : triggerGroupNames) {
                // 获取指定组中的所有触发器键
                Set<TriggerKey> triggerKeys = scheduler.getTriggerKeys(GroupMatcher.triggerGroupEquals(groupName));
                for (TriggerKey triggerKey : triggerKeys) {
                    Trigger trigger = scheduler.getTrigger(triggerKey);
                    JobDetail jobDetail = scheduler.getJobDetail(trigger.getJobKey());
                    // 在这里可以访问触发器和作业的相关信息
                    System.out.println("TriggerKey: " + triggerKey);
                    System.out.println("JobKey: " + jobDetail.getKey());
                    System.out.println("Trigger State: " + scheduler.getTriggerState(triggerKey));
                }
            }
        } catch (SchedulerException e) {
            e.printStackTrace();
        }*/
    }

    @Override
    public Result getJobsByAppId(String appId) {
        // GroupMatcher<TriggerKey> groupMatcher = GroupMatcher.triggerGroupEquals(appId);
        return getJobsByGroupMatcher(appId);
    }

    private Result getJobsByGroupMatcher(String appId) {
        Map<String, Object> hashAll = redisCache.getHashAll("jobVos");
        List<JobVo> jobVOS = new ArrayList<>();
        if (appId == null) {
            for (Map.Entry<String, Object> entry : hashAll.entrySet()) {
                jobVOS.add((JobVo) entry.getValue());
            }
        } else {
            for (Map.Entry<String, Object> entry : hashAll.entrySet()) {
                JobVo jobVo = (JobVo) entry.getValue();
                if (jobVo.getJobGroupName().equals(appId)) {
                    jobVOS.add(jobVo);
                }
            }
        }
        return Result.okResult(jobVOS);
    }


    @Autowired
    private HttpClientUtils httpClientUtils;
    @Value("${server.port}")
    private int port;

    @Override
    public void JobHeavyLoad() {
        String thisIpaddr = IpUtils.getIpaddr() + ":" + port;

        Map<String, Object> hashAll = redisCache.getHashAll(SystemConstant.CACHE_JOBS);
        for (Map.Entry<String, Object> entry : hashAll.entrySet()) {
            JobVo jobVo = (JobVo) entry.getValue();
            Map<String, Mail> params = new HashMap<>();
            params.put("mail",jobVo.getMail());
            if (thisIpaddr.equals(jobVo.getIpaddr())) {
                    loadJob(jobVo.getJobName(), jobVo.getJobGroupName(),jobVo.getJobName(), jobVo.getJobGroupName(),
                            jobVo.getCron(),jobVo.getStartTime(),jobVo.getEndTime(),params);
            } else {
                try {
                    String ans = httpClientUtils.get("http://" + jobVo.getIpaddr() + "/heart");
                    if (ans.equals("1"))continue;
                } catch (Exception e) {
                    loadJob(jobVo.getJobName(), jobVo.getJobGroupName(),jobVo.getJobName(), jobVo.getJobGroupName(),
                            jobVo.getCron(),jobVo.getStartTime(),jobVo.getEndTime(),params);
                    jobVo.setIpaddr(IpUtils.getIpaddr()+":"+port);
                    redisCache.setCacheMapValue(SystemConstant.CACHE_JOBS, jobVo.getJobName(), jobVo);
                }
            }
        }

    }
    @Autowired
    private ConfigProperties configProperties;
    private void loadJob(String jobName, String jobGroupName, // 工作名 ， 工作组名(调度源)
                         String triggerName, String triggerGroupName, // 触发器名 ， 触发器组名(调度源)
                          String cron,  // 自定义工作类 ，
                         Date startTime, Date endTime,
                         Map<String, Mail> params){
        Class jobClass = null;
        if (jobGroupName.equals(configProperties.getAppId())){
            jobClass = LocalSendMailJob.class;
        }else {
            jobClass = RemoteSendMailJob.class;
        }
        try {
            // 创建作业对象
            JobDetail jobDetail = JobBuilder.newJob(jobClass).withIdentity(jobName, jobGroupName).build();
            // 创建触发器
            if (params != null) {
                // 将参数传递给作业
                jobDetail.getJobDataMap().putAll(params);
            }
            // 构建触发器
            TriggerBuilder<Trigger> triggerBuilder = TriggerBuilder.newTrigger();
            // 触发器名,触发器组
            triggerBuilder.withIdentity(triggerName, triggerGroupName);
            if (startTime != null){
                triggerBuilder.startAt(startTime);
            }else {
                triggerBuilder.startNow();
            }
            if (endTime != null){
                triggerBuilder.endAt(endTime);
            }
            // 触发器时间设定   cron 为空就只执行一次
            if (cron != null && !cron.isEmpty()) {
                // 使用cron表达式创建触发器
                triggerBuilder.withSchedule(CronScheduleBuilder.cronSchedule(cron));
            } else {
                // 创建一个只执行一次的触发器
                triggerBuilder.withSchedule(SimpleScheduleBuilder.simpleSchedule().withIntervalInSeconds(0).withRepeatCount(0));
            }
            // 创建Trigger对象
            Trigger trigger = triggerBuilder.build();
            // 启动
            scheduler.start();
            scheduler.scheduleJob(jobDetail, trigger);
        }catch (Exception e){
            e.printStackTrace();
            throw new SystemException(AppHttpCodeEnum.JOB_HEAVYLOAD_ERROR);
        }

    }
/*    private Result getJobsByGroupMatcher(GroupMatcher<TriggerKey> groupMatcher) {
        List<JobVo> jobVOS = new ArrayList<>();
        try {
            Set<TriggerKey> triggerKeys = scheduler.getTriggerKeys(groupMatcher);
            for (TriggerKey triggerKey : triggerKeys) {
                Trigger trigger = scheduler.getTrigger(triggerKey);
                JobDetail jobDetail = scheduler.getJobDetail(trigger.getJobKey());
                JobVo jobVO = new JobVo();
                Mail mail = (Mail) jobDetail.getJobDataMap().get("mail");
                jobVO.setJobName(jobDetail.getKey().getName());
                jobVO.setJobGroupName(jobDetail.getKey().getGroup());
                jobVO.setState(scheduler.getTriggerState(triggerKey).ordinal());
                jobVO.setNextFireTime(trigger.getNextFireTime());
                jobVO.setPreviousFireTime(trigger.getPreviousFireTime());
                jobVO.setStartTime(trigger.getStartTime());
                jobVO.setEndTime(trigger.getEndTime());
                jobVO.setMail(mail);
                jobVOS.add(jobVO);
            }
        } catch (SchedulerException e) {
            return Result.okResult(jobVOS);
        }
        return Result.okResult(jobVOS);
    }*/

}
