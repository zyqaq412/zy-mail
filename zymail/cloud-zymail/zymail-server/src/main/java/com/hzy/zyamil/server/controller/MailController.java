package com.hzy.zyamil.server.controller;

import com.hzy.zyamil.common.exception.SystemException;
import com.hzy.zyamil.common.model.dto.MailPage;
import com.hzy.zyamil.common.model.entity.Mail;
import com.hzy.zyamil.common.utils.CodeEnum;
import com.hzy.zyamil.common.utils.Result;
import com.hzy.zyamil.server.api.ZymailClientApi;
import com.hzy.zyamil.server.config.ConfigProperties;
import com.hzy.zyamil.server.model.entity.Template;
import com.hzy.zyamil.server.service.LocalMailService;
import com.hzy.zyamil.server.service.MailService;
import com.hzy.zyamil.server.service.RemoteMailService;
import com.hzy.zyamil.server.service.TemplateService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.io.IOException;
import java.util.Date;

/**
 * @title: MailController
 * @Author zxwyhzy
 * @Date: 2023/9/24 16:32
 * @Version 1.0
 */
@RequestMapping("/mails")
@RestController
@Slf4j
public class MailController {
    @Autowired
    private ConfigProperties configProperties;
    @Autowired
    private LocalMailService localMailService;
    @Autowired
    private RemoteMailService remoteMailService;
    @Autowired
    private TemplateService templateService;
    @Autowired
    private  MailService mailService;

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

    @PostMapping("/list")
    public Result mailList(@RequestBody MailPage mailPage)  {
        log.info("获取历史邮件-- 测试负载均衡");
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


    @PostMapping("/saveMail")
    public Result save(@RequestBody Mail mail) {

        mailService.save(mail);

        return Result.okResult();
    }
    @Autowired
    private ZymailClientApi zymailClientApi;
    @PostMapping("/send")
    public Result send(@RequestBody Mail mail) {

        // 是本地源
        if (mail.getSource().equals(configProperties.getAppId())) {
            localMailService.sendHtmlMail(mail.getToUser(), mail.getSubject(), mail.getContent());
        } else {
            try {
                zymailClientApi.sendEmail(mail);
            } catch (IOException e) {
                throw new SystemException(CodeEnum.SYSTEM_ERROR,"远程发送邮件失败");
            }
        }
        return Result.okResult();
    }
}
