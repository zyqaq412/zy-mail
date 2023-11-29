package com.hzy.zyamil.server.service.impl;

import com.hzy.zyamil.common.model.entity.Mail;
import com.hzy.zyamil.server.clients.JobClient;
import com.hzy.zyamil.server.config.ConfigProperties;
import com.hzy.zyamil.server.service.LocalMailService;
import com.hzy.zyamil.server.service.MailService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.stereotype.Service;

import javax.mail.MessagingException;
import javax.mail.internet.MimeMessage;
import java.util.Date;

/**
 * @title: localMailServiceImpl
 * @Author zxwyhzy
 * @Date: 2023/9/24 17:08
 * @Version 1.0
 */
@Service
@Slf4j
public class LocalMailServiceImpl implements LocalMailService {
    @Autowired
    private JavaMailSender javaMailSender;
    @Autowired
    private ConfigProperties configProperties;
    @Autowired
    private MailService mailService;

    //
    @Autowired
    private JobClient jobClient;
    @Override
    public void sendMail(Mail mail) {
        if (!mail.getTimer()) {
            // 未定时直接发送
            sendHtmlMail(mail.getToUser(), mail.getSubject(), mail.getContent());
            mail.setSendTime(new Date());
            mailService.save(mail);
        } else {
            // TODO 本地调度源定时邮件
            // createSendMailJob(mail);
            jobClient.addJob(mail);
        }
    }


    @Override
    public void sendHtmlMail(String toUser, String subject, String content) {
        MimeMessage message = javaMailSender.createMimeMessage();
        try {
            MimeMessageHelper helper = new MimeMessageHelper(message, true);
            helper.setFrom(configProperties.getMail().getUsername());
            helper.setTo(toUser);
            helper.setSubject(subject);
            helper.setText(content, true);
        } catch (MessagingException e) {
            e.printStackTrace();
        }
        javaMailSender.send(message);
    }
}
