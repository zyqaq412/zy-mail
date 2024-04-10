package com.hzy.zymail.client.config;

import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;

/**
 * @title: ConfigProperties
 * @Author zxwyhzy
 * @Date: 2023/9/23 15:26
 * @Version 1.0
 */
@Data
@Component
@ConfigurationProperties(prefix = "zymail")
public class ConfigProperties {

    private String appId;

    private MailProperties mail = new MailProperties();

    private ServerProperties server = new ServerProperties();

    private Boolean Access = false;

    @Data
    public class MailProperties {

        private String host;

        private String username;

        private String password;

    }

    @Data
    public class ServerProperties {
        private String url;
    }
}
