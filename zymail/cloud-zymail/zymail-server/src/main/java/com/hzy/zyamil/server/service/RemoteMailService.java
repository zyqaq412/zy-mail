package com.hzy.zyamil.server.service;


import com.hzy.zyamil.common.model.entity.Mail;

/**
 * @title: remoteMailService
 * @Author zxwyhzy
 * @Date: 2023/9/24 17:07
 * @Version 1.0
 */
public interface RemoteMailService {
    /**
     * 发送邮件（定时/单次）
     * @param mail
     */
    void sendMail(Mail mail);
}
