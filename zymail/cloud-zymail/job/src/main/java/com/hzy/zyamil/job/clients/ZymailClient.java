package com.hzy.zyamil.job.clients;

import com.hzy.zyamil.common.model.entity.Mail;
import com.hzy.zyamil.common.utils.Result;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.http.MediaType;
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
    @PostMapping(value = "/mails/send",consumes = MediaType.APPLICATION_JSON_VALUE)
    Result sendEmail(@RequestBody Mail mail);
    @PostMapping(value = "/mails/saveMail",consumes = MediaType.APPLICATION_JSON_VALUE)
    Result save(@RequestBody Mail mail);
}
