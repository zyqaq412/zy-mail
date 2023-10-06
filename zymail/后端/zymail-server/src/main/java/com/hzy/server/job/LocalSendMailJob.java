package com.hzy.server.job;

import com.hzy.server.config.ConfigProperties;
import com.hzy.server.model.entity.Mail;
import com.hzy.server.service.LocalMailService;
import com.hzy.server.service.MailLogService;
import com.hzy.server.service.MailService;
import com.hzy.server.utils.LogTemplate;
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
public class LocalSendMailJob implements Job {
    @Autowired
    private LocalMailService localMailService;
    @Autowired
    private MailService mailService;

    @Override
    public void execute(JobExecutionContext jobExecutionContext) throws JobExecutionException {
        log.info("本地-定时任务开始执行");
        JobDataMap dataMap = jobExecutionContext.getMergedJobDataMap();
        Mail mail = (Mail) dataMap.get("mail");
        localMailService.sendHtmlMail(mail.getToUser(), mail.getSubject(), mail.getContent());
        mail.setSendTime(new Date());
        mailService.save(mail);
        log.info("本地-定时任务执行完毕");
        after(jobExecutionContext);
    }
    @Autowired
    private MailLogService logService;
    @Autowired
    private ConfigProperties configProperties;


    private void after(JobExecutionContext jobExecutionContext) {
        if (jobExecutionContext.getNextFireTime() == null ||
                jobExecutionContext.getNextFireTime().compareTo(new Date()) < 0){
            JobDetail jobDetail = jobExecutionContext.getJobDetail();
            logService.info(configProperties.getAppId(),
                    LogTemplate.endJobTemplate(jobDetail.getKey().getName(),
                            jobDetail.getKey().getGroup(),
                            jobDetail.getJobClass()));
        }
    }

}
