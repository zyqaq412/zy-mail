package com.hzy.zyamil.job.service.impl;

import com.hzy.zyamil.common.constant.SystemConstant;
import com.hzy.zyamil.common.exception.SystemException;
import com.hzy.zyamil.common.model.entity.Mail;
import com.hzy.zyamil.common.model.vo.JobVo;
import com.hzy.zyamil.common.utils.CodeEnum;
import com.hzy.zyamil.common.utils.LogTemplate;
import com.hzy.zyamil.common.utils.Result;
import com.hzy.zyamil.job.jobs.SendMailJob;
import com.hzy.zyamil.job.service.JobService;
import com.hzy.zyamil.job.service.LogService;
import com.hzy.zyamil.job.utils.RedisCache;
import org.quartz.*;
import org.quartz.impl.matchers.GroupMatcher;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Lazy;
import org.springframework.stereotype.Service;

import java.util.*;

/**
 * @title: JobServiceImpl
 * @Author zxwyhzy
 * @Date: 2023/11/29 16:09
 * @Version 1.0
 */
@Service
public class JobServiceImpl implements JobService {
    @Autowired
    private Scheduler scheduler;
    @Autowired
    private RedisCache redisCache;
    // TODO 注入 logClients有循环依赖问题
    @Autowired
    @Lazy
    private LogService logService;

    @Override
    public Result getAllJobs() {
        GroupMatcher<TriggerKey> groupMatcher = GroupMatcher.anyTriggerGroup();
        return getJobsByGroupMatcher(groupMatcher);
    }

    @Override
    public Result getJobsByAppId(String appId) {
        GroupMatcher<TriggerKey> groupMatcher = GroupMatcher.triggerGroupEquals(appId);
        return getJobsByGroupMatcher(groupMatcher);
    }



    private Result getJobsByGroupMatcher(GroupMatcher<TriggerKey> groupMatcher) {
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
    }



    @Override
    public void JobHeavyLoad() {
        // TODO 这里重载的操作需要加分布式锁 防止多个实列同时重载一个任务

        Map<String, Object> hashAll = redisCache.getHashAll(SystemConstant.CACHE_JOBS);
        for (Map.Entry<String, Object> entry : hashAll.entrySet()) {
            JobVo jobVo = (JobVo) entry.getValue();
            Map<String, Mail> params = new HashMap<>();
            params.put("mail", jobVo.getMail());

            loadJob(jobVo.getJobName(), jobVo.getJobGroupName(), jobVo.getJobName(), jobVo.getJobGroupName(),
                    jobVo.getCron(), jobVo.getStartTime(), jobVo.getEndTime(), params);

            modifyState(jobVo.getJobName(), jobVo.getJobGroupName(),jobVo.getState());

            logService.warning(jobVo.getJobGroupName(), LogTemplate.heavyLoadJobTemplate(
                    jobVo.getJobName(), jobVo.getJobGroupName(), jobVo.getIpaddr()
            ));
        }

    }

    private void modifyState(String jobName, String jobGroupName, Integer state) {
        if (state == 2){
            TriggerKey triggerKey = TriggerKey.triggerKey(jobName, jobGroupName);
            try {
                scheduler.pauseTrigger(triggerKey);
            } catch (SchedulerException e) {
                throw new RuntimeException(e);
            }
        }
    }


    private void loadJob(String jobName, String jobGroupName, // 工作名 ， 工作组名(调度源)
                         String triggerName, String triggerGroupName, // 触发器名 ， 触发器组名(调度源)
                         String cron,  // 自定义工作类 ，
                         Date startTime, Date endTime,
                         Map<String, Mail> params) {
        Class jobClass = SendMailJob.class;

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
            if (startTime != null) {
                triggerBuilder.startAt(startTime);
            } else {
                triggerBuilder.startNow();
            }
            if (endTime != null) {
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

        } catch (Exception e) {
            e.printStackTrace();
            throw new SystemException(CodeEnum.JOB_HEAVYLOAD_ERROR);
        }
    }

}
