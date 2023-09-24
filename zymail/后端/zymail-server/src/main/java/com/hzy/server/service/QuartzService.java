package com.hzy.server.service;

import com.hzy.server.model.entity.Mail;

import java.util.Date;
import java.util.Map;

/**
 * @title: JobService
 * @Author zxwyhzy
 * @Date: 2023/9/24 22:08
 * @Version 1.0
 */
public interface QuartzService {


    /**
     * 添加一个定时任务
     * @param jobName          Job名
     * @param jobGroupName     Job组名
     * @param triggerName      触发器名
     * @param triggerGroupName 触发器组名
     * @param jobClass         自定义工作类
     * @param cron             设置定时规则
     * @param startTime        开始时间
     * @param endTime          结束时间
     * @param params           参数
     */
    void addJob(String jobName, String jobGroupName, String triggerName, String triggerGroupName,
                Class jobClass, String cron, Date startTime, Date endTime, Map<String, Mail> params);
}
