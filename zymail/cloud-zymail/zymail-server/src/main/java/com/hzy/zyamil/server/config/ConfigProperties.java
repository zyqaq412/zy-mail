package com.hzy.zyamil.server.config;

import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;

/**
 * @title: ConfigProperties
 * @Author zxwyhzy
 * @Date: 2023/9/24 17:11
 * @Version 1.0
 */
@Data
@Component
@ConfigurationProperties(prefix = "zymail")
public class ConfigProperties {
    private String appId;

    private MailProperties mail = new MailProperties();

    private AdminProperties admin = new AdminProperties();

    @Data
    public class MailProperties {

        private String host;

        private String username;

        private String password;

    }

    @Data
    public class AdminProperties {

        private String username;

        private String password;
    }
}
