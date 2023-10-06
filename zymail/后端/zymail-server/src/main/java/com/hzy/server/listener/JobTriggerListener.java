package com.hzy.server.listener;

import com.hzy.server.config.ConfigProperties;
import com.hzy.server.service.MailLogService;
import com.hzy.server.utils.LogTemplate;
import org.quartz.JobDetail;
import org.quartz.JobExecutionContext;
import org.quartz.Trigger;
import org.quartz.TriggerListener;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.Date;

/**
 * @title: JobTriggerListener
 * @Author zxwyhzy
 * @Date: 2023/10/5 22:36
 * @Version 1.0
 */
public class JobTriggerListener implements TriggerListener {

    @Override
    // 通常情况下，这个方法应该返回 TriggerListener 的名称
    public String getName() {
        return getClass().getSimpleName();
    }

    @Override
    // 定义触发器触发时的逻辑行为
    public void triggerFired(Trigger trigger, JobExecutionContext jobExecutionContext) {

    }

    @Override
    // 确定是否要阻止作业执行
    // 默认false 不阻止
    public boolean vetoJobExecution(Trigger trigger, JobExecutionContext jobExecutionContext) {
        return false;
    }

    @Override
    // 用于处理当触发器未按时触发作业时的情况。
    // Quartz框架会自动处理触发器的错过，可以使用该方法自定义错过逻辑
    public void triggerMisfired(Trigger trigger) {

    }

    @Autowired
    private MailLogService logService;
    @Autowired
    private ConfigProperties configProperties;

    /**
     *
     * @param trigger 触发器对象，包含有关触发器的信息。
     * @param jobExecutionContext 表示作业执行的上下文，包含有关作业执行的信息。
     * @param completedExecutionInstruction 表示作业执行完成时的指令或状态。
     */
    @Override
    // 方法用于处理触发器完成作业执行时的情况。这个方法会在作业执行完成后被调用
    public void triggerComplete(Trigger trigger, JobExecutionContext jobExecutionContext, Trigger.CompletedExecutionInstruction completedExecutionInstruction) {
        JobDetail jobDetail = jobExecutionContext.getJobDetail();
        System.out.println("triggerComplete");
        System.out.println("triggerComplete");
        System.out.println("triggerComplete");
        // 调度完成时记录日志
        if (trigger.getEndTime() == null || new Date().compareTo(trigger.getEndTime()) >= 0) {
            logService.info(configProperties.getAppId(),
                    LogTemplate.endJobTemplate(jobDetail.getKey().getName(),
                            jobDetail.getKey().getGroup(),
                            jobDetail.getJobClass()));
        }
    }
}
