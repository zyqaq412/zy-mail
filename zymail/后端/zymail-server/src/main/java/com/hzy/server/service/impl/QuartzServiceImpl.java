package com.hzy.server.service.impl;

import com.hzy.server.constant.AppHttpCodeEnum;
import com.hzy.server.exception.SystemException;
import com.hzy.server.model.entity.Mail;
import com.hzy.server.service.QuartzService;
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
            }catch (Exception e){
                e.printStackTrace();
                throw new SystemException(AppHttpCodeEnum.QUARTZ_ERROR);
            }

    }
}
