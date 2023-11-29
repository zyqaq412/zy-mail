package com.hzy.zyamil.job.jobs;

import com.hzy.zyamil.common.model.entity.Mail;
import com.hzy.zyamil.common.utils.LogTemplate;
import com.hzy.zyamil.job.clients.LogClients;
import com.hzy.zyamil.job.clients.ZymailClient;
import lombok.extern.slf4j.Slf4j;
import org.quartz.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.Date;

/**
 * @title: LocalSendMailJob
 * @Author zxwyhzy
 * @Date: 2023/9/24 22:04
 * @Version 1.0
 */
@Component
@Slf4j
public class SendMailJob implements Job {

    @Autowired
    private ZymailClient zymailClient;
    @Autowired
    private LogClients logClients;

    @Override
    public void execute(JobExecutionContext jobExecutionContext) throws JobExecutionException {
        log.info("定时任务开始执行");
        JobDataMap dataMap = jobExecutionContext.getMergedJobDataMap();
        Mail mail = (Mail) dataMap.get("mail");
        zymailClient.sendEmail(mail);
        mail.setSendTime(new Date());
        zymailClient.save(mail);
        log.info("本定时任务执行完毕");
        after(jobExecutionContext,mail.getSource());
    }

    private void after(JobExecutionContext jobExecutionContext,String appId) {
        // 下次执行时间为空 或者下次执行时间小于当前时间说明任务是最后一次执行
        if (jobExecutionContext.getNextFireTime() == null ||
                jobExecutionContext.getNextFireTime().compareTo(new Date()) < 0){
            JobDetail jobDetail = jobExecutionContext.getJobDetail();
            logClients.info(appId,
                    LogTemplate.endJobTemplate(jobDetail.getKey().getName(),
                            jobDetail.getKey().getGroup(),
                            jobDetail.getJobClass()));
        }
    }

}
