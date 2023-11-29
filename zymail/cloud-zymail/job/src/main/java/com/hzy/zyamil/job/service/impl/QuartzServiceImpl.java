package com.hzy.zyamil.job.service.impl;

import com.hzy.zyamil.common.model.dto.JobDto;
import com.hzy.zyamil.common.model.entity.Mail;
import com.hzy.zyamil.common.utils.Result;
import com.hzy.zyamil.job.service.QuartzService;
import org.quartz.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Date;
import java.util.Map;

/**
 * @title: QuartzServiceImpl
 * @Author zxwyhzy
 * @Date: 2023/11/29 16:10
 * @Version 1.0
 */
@SuppressWarnings("ALL")
@Service
public class QuartzServiceImpl implements QuartzService {

    @Autowired
    private Scheduler scheduler;
    @Override
    public void addJob(String jobName, String jobGroupName, String triggerName, String triggerGroupName, Class jobClass, String cron, Date startTime, Date endTime, Map<String, Mail> params) {
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
        }
    }

    @Override
    public void removeJob(String jobName, String jobGroupName, String triggerName, String triggerGroupName) {

    }

    @Override
    public void pauseJob(String jobName, String jobGroupName) {

    }

    @Override
    public void resumeJob(String jobName, String jobGroupName) {

    }

    @Override
    public int getSchedulerStatus() {
        return 0;
    }

    @Override
    public void startScheduler() {

    }

    @Override
    public void pauseScheduler() {

    }

    @Override
    public Result modifyJob(Integer radio, JobDto job) {
        return null;
    }
}
