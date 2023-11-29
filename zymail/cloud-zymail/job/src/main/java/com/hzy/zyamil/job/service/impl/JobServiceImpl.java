package com.hzy.zyamil.job.service.impl;

import com.hzy.zyamil.common.model.entity.Mail;
import com.hzy.zyamil.common.model.vo.JobVo;
import com.hzy.zyamil.common.utils.Result;
import com.hzy.zyamil.job.service.JobService;
import org.quartz.*;
import org.quartz.impl.matchers.GroupMatcher;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.Set;

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

    }
}
