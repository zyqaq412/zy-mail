package com.hzy.zyamil.job.service;

/**
 * @title: LogService
 * @Author zxwyhzy
 * @Date: 2023/11/30 18:41
 * @Version 1.0
 */
public interface LogService {
    void log(String appId, String content, Integer level);

    void error(String appId, String content);

    void warning(String appId, String content);

    void info(String appId, String content);
}
