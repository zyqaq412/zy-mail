package com.hzy.zyamil.job.clients;

import com.hzy.zyamil.common.model.entity.Mail;
import com.hzy.zyamil.common.utils.Result;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;

/**
 * @title: ZymailClient
 * @Author zxwyhzy
 * @Date: 2023/11/29 21:08
 * @Version 1.0
 */
@FeignClient("zymail-server")
public interface ZymailClient {
    /**
     * 发送邮件
     * @param mail
     * @return
     */
    @PostMapping("/mails")
    Result sendEmail(@RequestBody Mail mail);
    @PostMapping("/mails/save")
    Result save(@RequestBody Mail mail);
}
