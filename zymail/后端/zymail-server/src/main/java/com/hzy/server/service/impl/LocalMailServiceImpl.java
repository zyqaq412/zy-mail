package com.hzy.server.service.impl;

import com.hzy.server.config.ConfigProperties;
import com.hzy.server.model.entity.Mail;
import com.hzy.server.service.LocalMailService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.stereotype.Service;

import javax.mail.MessagingException;
import javax.mail.internet.MimeMessage;

/**
 * @title: localMailServiceImpl
 * @Author zxwyhzy
 * @Date: 2023/9/24 17:08
 * @Version 1.0
 */
@Service
public class LocalMailServiceImpl implements LocalMailService {
    @Autowired
    private JavaMailSender javaMailSender;
    @Autowired
    private ConfigProperties configProperties;
    @Override
    public void sendMail(Mail mail) {
        if (!mail.getTimer()){
            // 未定时直接发送
            sendHtmlMail(mail.getToUser(), mail.getSubject(), mail.getContent());
        }else {
            // TODO 本地调度源定时邮件

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
