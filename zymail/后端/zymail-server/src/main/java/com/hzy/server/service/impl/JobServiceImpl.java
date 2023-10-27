package com.hzy.server.service.impl;

import com.hzy.server.model.entity.JobVo;
import com.hzy.server.service.JobService;
import com.hzy.server.utils.RedisCache;
import com.hzy.server.utils.Result;
import org.quartz.Scheduler;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

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
        if (appId == null){
            for (Map.Entry<String, Object> entry : hashAll.entrySet()){
                jobVOS.add((JobVo) entry.getValue());
            }
        }else {
            for (Map.Entry<String, Object> entry : hashAll.entrySet()){
               JobVo jobVo = (JobVo) entry.getValue();
               if (jobVo.getJobGroupName().equals(appId)){
                   jobVOS.add(jobVo);
               }
            }
        }
        return Result.okResult(jobVOS);
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
