package com.hzy.zymail.client.api;

import com.hzy.zymail.client.config.ConfigProperties;
import com.hzy.zymail.client.config.MailSenderConfig;
import com.hzy.zymail.client.model.entity.Mail;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Import;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.scheduling.annotation.Async;

import javax.mail.internet.MimeMessage;

/**
 * @title: ZymailClient
 * @Author zxwyhzy
 * @Date: 2023/9/23 15:29
 * @Version 1.0
 */
@EnableConfigurationProperties({ConfigProperties.class})
@Import(MailSenderConfig.class)
@Slf4j
public class ZymailClient {

    @Autowired
    private JavaMailSender javaMailSender;
    @Autowired
    private ConfigProperties configProperties;
    @Autowired
    private ZymailServerApi zymailServerApi;

    @Async
    public void sendMail(Mail mail){
        try {
            if (mail.getTimer()){
                mail.setSource(configProperties.getAppId());
                zymailServerApi.sendMail(mail);
                return;
            }
            // 创建简单邮件消息
            MimeMessage message  = javaMailSender.createMimeMessage();
            MimeMessageHelper helper = new MimeMessageHelper(message, true);
            //谁要接收
            helper.setFrom(configProperties.getMail().getUsername());
            helper.setTo(mail.getToUser());
            //邮件标题
            helper.setSubject(mail.getSubject());
            //邮件内容
            helper.setText(mail.getContent(),true);
            // 可以添加附件
            // helper.addAttachment("附件名称", new File("附件路径"));
            javaMailSender.send(message);
        }catch (Exception e){
            log.error("客户端邮件发送失败");
            e.printStackTrace();
        }
    }
}
