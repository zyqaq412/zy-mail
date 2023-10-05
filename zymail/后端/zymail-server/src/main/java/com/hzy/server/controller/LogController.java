package com.hzy.server.controller;

import com.hzy.server.model.dto.MailPage;
import com.hzy.server.service.MailLogService;
import com.hzy.server.utils.Result;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

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

    @PostMapping("/{appId}")
    public Result getPageLogs(@RequestBody MailPage mailPage,@PathVariable String appId) {
        return mailLogService.getPageLogs(mailPage,appId);
    }

    @PostMapping()
    public Result getPageLogs(@RequestBody MailPage mailPage) {
        return mailLogService.getPageLogs(mailPage,null);
    }
/*    @GetMapping
    public Result getAllLogs() {
        return mailLogService.getAllLogs();
    }

    // 获取指定调度源的日志
    @GetMapping("/{appId}")
    public Result getLogsByAppId(@PathVariable String appId) {
        return mailLogService.getLogsByAppId(appId);
    }*/
}
