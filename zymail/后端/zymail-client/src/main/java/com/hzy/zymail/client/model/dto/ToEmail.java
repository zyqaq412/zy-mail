package com.hzy.zymail.client.model.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;

/**
 * @title: ToMail
 * @Author zxwyhzy
 * @Date: 2023/9/23 15:22
 * @Version 1.0
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
public class ToEmail implements Serializable {
    /**
     * 邮件接收方
     */
    private String toUser;
    /**
     * 邮件主题
     */
    private String subject;
    /**
     * 邮件内容
     */
    private String content;
}