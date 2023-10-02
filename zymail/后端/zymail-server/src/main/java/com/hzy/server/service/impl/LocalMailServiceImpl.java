package com.hzy.server.service.impl;

import com.hzy.server.config.ConfigProperties;
import com.hzy.server.job.LocalSendMailJob;
import com.hzy.server.model.entity.Mail;
import com.hzy.server.service.LocalMailService;
import com.hzy.server.service.MailService;
import com.hzy.server.service.QuartzService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.stereotype.Service;

import javax.mail.MessagingException;
import javax.mail.internet.MimeMessage;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

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
    private QuartzService quartzService;
    @Autowired
    private MailService mailService;

    @Override
    public void sendMail(Mail mail) {
        if (!mail.getTimer()) {
            // 未定时直接发送
            sendHtmlMail(mail.getToUser(), mail.getSubject(), mail.getContent());
            mail.setSendTime(new Date());
            mailService.save(mail);
        } else {
            // TODO 本地调度源定时邮件
            createSendMailJob(mail);
        }
    }

    // 创建 定时任务
    private void createSendMailJob(Mail mail) {
        Map<String, Mail> params = new HashMap<>();
        params.put("mail", mail);
        String uniKey = UUID.randomUUID().toString();
        log.info("添加本地任务");
        quartzService.addJob("sendMail-" + uniKey,
                mail.getSource(),
                "sendMail-" + uniKey,
                mail.getSource(),
                LocalSendMailJob.class,
                mail.getCron(),
                mail.getStartTime(),
                mail.getEndTime(),
                params);
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
