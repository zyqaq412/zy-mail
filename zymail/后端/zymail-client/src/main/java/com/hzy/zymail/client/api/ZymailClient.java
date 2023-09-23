package com.hzy.zymail.client.api;

import com.hzy.zymail.client.config.ConfigProperties;
import com.hzy.zymail.client.config.MailSenderConfig;
import com.hzy.zymail.client.model.dto.ToEmail;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Import;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;

/**
 * @title: ZymailClient
 * @Author zxwyhzy
 * @Date: 2023/9/23 15:29
 * @Version 1.0
 */
@EnableConfigurationProperties({ConfigProperties.class})
@Import(MailSenderConfig.class)
public class ZymailClient {

    @Autowired
    private JavaMailSender javaMailSender;
    @Autowired
    private ConfigProperties configProperties;

    public void sendMail(ToEmail toEmail){
        // 创建简单邮件消息
        SimpleMailMessage message = new SimpleMailMessage();
        //谁要接收
        message.setTo(toEmail.getToUser());
        //邮件标题
        message.setSubject(toEmail.getSubject());
        //邮件内容
        message.setText(toEmail.getContent());

        message.setFrom(configProperties.getMail().getUsername());

        javaMailSender.send(message);
    }
}
