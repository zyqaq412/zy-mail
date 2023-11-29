package com.hzy.zyamil.server;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.openfeign.EnableFeignClients;

/**
 * @title: ZymailServerApp
 * @Author zxwyhzy
 * @Date: 2023/9/23 16:57
 * @Version 1.0
 */
@SpringBootApplication
@EnableFeignClients
public class ZymailServerApp {
    public static void main(String[] args) {
        SpringApplication.run(ZymailServerApp.class, args);
    }
}
