package com.hzy.zyamil.log.controller;


import com.hzy.zyamil.common.model.dto.MailPage;
import com.hzy.zyamil.common.utils.Result;
import com.hzy.zyamil.log.service.LogService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

/**
 * @title: LogController
 * @Author zxwyhzy
 * @Date: 2023/11/29 20:27
 * @Version 1.0
 */
@RestController
@RequestMapping("/logs")
public class LogController {
    @Autowired
    private LogService logService;

    @PostMapping("/{appId}")
    public Result getPageLogs(@RequestBody MailPage mailPage, @PathVariable String appId) {
        return logService.getPageLogs(mailPage,appId);
    }

    @PostMapping()
    public Result getPageLogs(@RequestBody MailPage mailPage) {
        return logService.getPageLogs(mailPage,null);
    }

    @PostMapping("/log")
    public Result addLog(String appId, String content,Integer level) {
        logService.log(appId,content,level);
        return Result.okResult();
    }

}
