package com.hzy.zymail.client.config;

import com.hzy.zymail.client.api.ZymailClient;
import lombok.Data;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.condition.ConditionalOnClass;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.JavaMailSenderImpl;

/**
 * @title: MailSenderConfig
 * @Author zxwyhzy
 * @Date: 2023/9/23 15:27
 * @Version 1.0
 */
@Data
@Configuration
@EnableConfigurationProperties({ConfigProperties.class})
@ConditionalOnClass(ZymailClient.class)
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
