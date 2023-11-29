package com.hzy.zyamil;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.openfeign.EnableFeignClients;

/**
 * Hello world!
 */

@SpringBootApplication
@EnableFeignClients
public class JobApplication {
    public static void main(String[] args) {
        SpringApplication.run(JobApplication.class);
    }
}
