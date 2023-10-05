package com.hzy.server.controller;

import com.hzy.server.service.MailLogService;
import com.hzy.server.utils.Result;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * @title: LogController
 * @Author zxwyhzy
 * @Date: 2023/10/5 21:29
 * @Version 1.0
 */
@RestController
@RequestMapping("/logs")
public class LogController {
    @Autowired
    private MailLogService mailLogService;
    @GetMapping
    public Result getAllLogs() {
        return mailLogService.getAllLogs();
    }

    // 获取指定调度源的日志
    @GetMapping("/{appId}")
    public Result getLogsByAppId(@PathVariable String appId) {
        return mailLogService.getLogsByAppId(appId);
    }
}
