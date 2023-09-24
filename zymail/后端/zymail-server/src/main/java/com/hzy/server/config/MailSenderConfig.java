package com.hzy.server.config;

import lombok.Data;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.JavaMailSenderImpl;

/**
 * @title: MailSenderConfig
 * @Author zxwyhzy
 * @Date: 2023/9/24 17:14
 * @Version 1.0
 */
@Data
@Configuration
public class MailSenderConfig {
    @Autowired
    private ConfigProperties configProperties;

    @Bean
    public JavaMailSender javaMailSender() {
        JavaMailSenderImpl javaMailSender = new JavaMailSenderImpl();
        javaMailSender.setHost(configProperties.getMail().getHost());
        javaMailSender.setUsername(configProperties.getMail().getUsername());
        javaMailSender.setPassword(configProperties.getMail().getPassword());
        javaMailSender.setDefaultEncoding("utf-8");
        return javaMailSender;
    }

}
