package com.hzy.server.job;

import com.hzy.server.api.ZymailClientApi;
import com.hzy.server.model.entity.Mail;
import com.hzy.server.service.MailService;
import lombok.extern.slf4j.Slf4j;
import org.quartz.Job;
import org.quartz.JobDataMap;
import org.quartz.JobExecutionContext;
import org.quartz.JobExecutionException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.io.IOException;
import java.util.Date;

/**
 * @title: LocalSendMailJob
 * @Author zxwyhzy
 * @Date: 2023/9/24 22:04
 * @Version 1.0
 */
@Component
@Slf4j
public class RemoteSendMailJob implements Job {
    @Autowired
    private ZymailClientApi zymailClientApi;
    @Autowired
    private MailService mailService;
    @Override
    public void execute(JobExecutionContext jobExecutionContext) throws JobExecutionException {
        log.info("远程-定时任务开始执行");
        JobDataMap dataMap = jobExecutionContext.getMergedJobDataMap();
        Mail mail = (Mail) dataMap.get("mail");
        try {
            zymailClientApi.sendEmail(mail);
            mail.setSendTime(new Date());
            mailService.save(mail);
        } catch (IOException e) {
            e.printStackTrace();
        }
        log.info("远程-定时任务执行完毕");
    }
}
