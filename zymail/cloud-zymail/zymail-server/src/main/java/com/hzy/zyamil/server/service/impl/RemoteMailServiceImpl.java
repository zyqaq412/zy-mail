package com.hzy.zyamil.server.service.impl;

import com.hzy.zyamil.common.model.entity.Mail;
import com.hzy.zyamil.server.api.ZymailClientApi;
import com.hzy.zyamil.server.clients.JobClient;
import com.hzy.zyamil.server.service.MailService;
import com.hzy.zyamil.server.service.RemoteMailService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.util.Date;

/**
 * @title: remoteMailServiceImpl
 * @Author zxwyhzy
 * @Date: 2023/9/24 17:08
 * @Version 1.0
 */
@Service
@Slf4j
public class RemoteMailServiceImpl implements RemoteMailService {

    @Autowired
    private ZymailClientApi zymailClientApi;

    @Autowired
    private MailService mailService;
    @Autowired
    private JobClient jobClient;
    @Override
    public void sendMail(Mail mail) {
        if (!mail.getTimer()){
            try {
                zymailClientApi.sendEmail(mail);
                mail.setSendTime(new Date());
                mailService.save(mail);
            } catch (IOException e) {
                throw new RuntimeException(e);
            }
        }else {
            // TODO 远程调度源定时发送
            jobClient.addJob(mail);
        }
    }
}
