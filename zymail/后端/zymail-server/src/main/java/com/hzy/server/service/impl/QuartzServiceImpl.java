package com.hzy.server.service.impl;

import com.hzy.server.constant.AppHttpCodeEnum;
import com.hzy.server.exception.SystemException;
import com.hzy.server.model.entity.Mail;
import com.hzy.server.service.MailLogService;
import com.hzy.server.service.QuartzService;
import com.hzy.server.utils.LogTemplate;
import lombok.extern.slf4j.Slf4j;
import org.quartz.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Date;
import java.util.Map;

/**
 * @title: JobServiceImpl
 * @Author zxwyhzy
 * @Date: 2023/9/24 22:08
 * @Version 1.0
 */
@Service
@Slf4j
public class QuartzServiceImpl implements QuartzService {
    @Autowired
    private Scheduler scheduler;
    @Autowired
    private MailLogService logService;
    @Override
    public void addJob(String jobName, String jobGroupName, // 工作名 ， 工作组名(调度源)
                       String triggerName, String triggerGroupName, // 触发器名 ， 触发器组名(调度源)
                       Class jobClass, String cron,  // 自定义工作类 ，
                       Date startTime, Date endTime,
                       Map<String, Mail> params) { // 参数(邮件)
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
                log.info("任务设置成功");
                // 日志管理  添加启动日志
                logService.info(triggerGroupName, LogTemplate.startJobTemplate(
                        jobName,jobGroupName,jobClass,cron,startTime,endTime
                ));
            }catch (Exception e){
                e.printStackTrace();
                throw new SystemException(AppHttpCodeEnum.QUARTZ_ERROR);
            }

    }

    @Override
    public void removeJob(String jobName, String jobGroupName, String triggerName, String triggerGroupName) {
        try {

            TriggerKey triggerKey = TriggerKey.triggerKey(triggerName, triggerGroupName);
            // 停止触发器
            scheduler.pauseTrigger(triggerKey);
            // 移除触发器
            scheduler.unscheduleJob(triggerKey);
            // 删除任务
            scheduler.deleteJob(JobKey.jobKey(jobName, jobGroupName));
            log.info("删除任务:"+JobKey.jobKey(jobName));
            // 日志管理  添加删除日志
            logService.error(jobGroupName,LogTemplate.delJobTemplate(jobName,jobGroupName));
        } catch (Exception e) {
            throw new SystemException(AppHttpCodeEnum.QUARTZ_ERROR);
        }
    }

    @Override
    public void pauseJob(String jobName, String jobGroupName) {
        TriggerKey triggerKey = TriggerKey.triggerKey(jobName, jobGroupName);
        // 停止触发器
        try {
            scheduler.pauseTrigger(triggerKey);
            // 日志管理  添加暂停日志
            logService.warning(jobGroupName,LogTemplate.pauseJobTemplate(jobName,jobGroupName));
        } catch (SchedulerException e) {
            throw new SystemException(AppHttpCodeEnum.QUARTZ_ERROR);
        }
    }

    @Override
    public void resumeJob(String jobName, String jobGroupName) {
        TriggerKey triggerKey = TriggerKey.triggerKey(jobName, jobGroupName);
        // 恢复触发器
        try {
            scheduler.resumeTrigger(triggerKey);
            // 日志管理  添加恢复日志
            logService.info(jobGroupName,LogTemplate.resumeJobTemplate(jobName,jobGroupName));
        } catch (SchedulerException e) {
            throw new SystemException(AppHttpCodeEnum.QUARTZ_ERROR);
        }
    }

    @Override
    public int getSchedulerStatus() {
        try {
            if (scheduler.isInStandbyMode()) {
                return 1;
            } else if (scheduler.isStarted()) {
                return 0;
            } else if (scheduler.isShutdown()) {
                return 2;
            } else {
                return 3;
            }
        } catch (Exception e) {
            throw new SystemException(AppHttpCodeEnum.QUARTZ_ERROR);
        }
    }

    @Override
    public void startScheduler() {
        try {
            if (!scheduler.isShutdown()) {
                scheduler.start();
            }
        } catch (Exception e) {
            throw new SystemException(AppHttpCodeEnum.QUARTZ_ERROR);
        }
    }

    @Override
    public void pauseScheduler() {
        try {
            if (!scheduler.isShutdown()) {
                scheduler.standby();
            }
        } catch (Exception e) {
            throw new SystemException(AppHttpCodeEnum.QUARTZ_ERROR);
        }
    }
}
