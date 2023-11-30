package com.hzy.zyamil.job.service.impl;

import com.hzy.zyamil.common.constant.LogConstant;
import com.hzy.zyamil.job.clients.LogClients;
import com.hzy.zyamil.job.service.LogService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

/**
 * @title: LogServiceImpl
 * @Author zxwyhzy
 * @Date: 2023/11/30 18:41
 * @Version 1.0
 */
@Service
public class LogServiceImpl implements LogService {
    @Autowired
    private LogClients logClients;

    @Override
    public void log(String appId, String content, Integer level) {
        logClients.log(appId, content, level);
    }

    @Override
    public void error(String appId, String content) {
        log(appId,content, LogConstant.ERROR_LEVEL);
    }

    @Override
    public void warning(String appId, String content) {
        log(appId,content, LogConstant.ERROR_LEVEL);
    }

    @Override
    public void info(String appId, String content) {
        log(appId,content, LogConstant.ERROR_LEVEL);
    }
}
