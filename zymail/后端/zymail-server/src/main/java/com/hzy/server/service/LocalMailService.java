package com.hzy.server.service;
import com.hzy.server.model.entity.Mail;
/**
 * @title: localMailService
 * @Author zxwyhzy
 * @Date: 2023/9/24 17:07
 * @Version 1.0
 */
public interface LocalMailService {
    /**
     * 发送邮件（定时/单次）
     * @param mail
     */
    void sendMail(Mail mail);

    /**
     *  发送邮件
     * @param toUser
     * @param subject
     * @param content
     */
    void sendHtmlMail(String toUser, String subject, String content);
}
