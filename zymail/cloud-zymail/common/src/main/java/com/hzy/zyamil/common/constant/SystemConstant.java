package com.hzy.zyamil.common.constant;

/**
 * @title: SystemConstant 系统常量
 * @Author zxwyhzy
 * @Date: 2023/9/23 23:15
 * @Version 1.0
 */
public interface SystemConstant {
    // 调度源key
    String SOURCES_KEY = "sources";
    // 邮件发送工作 前缀
    String JOB_SENDMAIL = "sendMail-";
    String MAIL_KEY = "mails:mailId-";
    Integer CACHE_EXPIRE_TIME = 1000 * 60 * 30;

    String CACHE_JOBS = "jobVos";

    String Lock__JOB = "lock-job";
}
