package com.hzy.zymail.client.controller;

import com.hzy.zymail.client.api.ZymailClient;
import com.hzy.zymail.client.model.entity.Mail;
import com.hzy.zymail.client.utils.Result;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * @title: MailController
 * @Author zxwyhzy
 * @Date: 2023/9/24 19:04
 * @Version 1.0
 */

@RestController
@RequestMapping("/mails")
public class MailController {
    @Autowired
    private ZymailClient zymailClient;

    @PostMapping
    public Result sendEmail(@RequestBody Mail mail) {
        zymailClient.sendMail(mail);
        return Result.okResult();
    }

}
