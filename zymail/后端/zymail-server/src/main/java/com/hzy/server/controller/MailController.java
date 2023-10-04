package com.hzy.server.controller;

import com.hzy.server.config.ConfigProperties;
import com.hzy.server.model.dto.MailPage;
import com.hzy.server.model.entity.Mail;
import com.hzy.server.model.entity.Template;
import com.hzy.server.service.LocalMailService;
import com.hzy.server.service.MailService;
import com.hzy.server.service.RemoteMailService;
import com.hzy.server.service.TemplateService;
import com.hzy.server.utils.Result;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.Date;

/**
 * @title: MailController
 * @Author zxwyhzy
 * @Date: 2023/9/24 16:32
 * @Version 1.0
 */
@RequestMapping("/mails")
@RestController
public class MailController {
    @Autowired
    private ConfigProperties configProperties;
    @Autowired
    private LocalMailService localMailService;
    @Autowired
    private RemoteMailService remoteMailService;
    @Autowired
    private TemplateService templateService;

    /**
     * 给管理界面的接口
     *
     * @param mail
     * @return
     */
    @PostMapping
    public Result sendEmail(@RequestBody Mail mail) {

        // 是本地源
        if (mail.getSource().equals(configProperties.getAppId())) {
            localMailService.sendMail(mail);
        } else {
            remoteMailService.sendMail(mail);
        }
        return Result.okResult();
    }

    @Autowired
    private MailService mailService;

    @PostMapping("/list")
    public Result mailList(@RequestBody MailPage mailPage) {

        return mailService.getList(mailPage);
    }

    @GetMapping("/{id}")
    public Result getMailById(@PathVariable("id") Long id) {
        return mailService.getMailById(id);
    }

    @PostMapping("/save")
    public Result saveTemplate(@RequestBody Template template) {
        template.setCreateTime(new Date());
        templateService.save(template);
        return Result.okResult();
    }

    @GetMapping
    public Result getTemplates() {
        return templateService.getTemplates();
    }
}
