package com.hzy.zyamil.job.clients;

import com.hzy.zyamil.common.utils.Result;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;

/**
 * @title: LogClients
 * @Author zxwyhzy
 * @Date: 2023/11/29 20:34
 * @Version 1.0
 */
@FeignClient("log-server")
public interface LogClients {

    @PostMapping("/logs/warning")
    Result warning(@RequestParam("appId") String appId, @RequestParam("content") String content);

    @PostMapping("/logs/error")
    Result error(@RequestParam("appId") String appId, @RequestParam("content") String content);

    @PostMapping("/logs/info")
    Result info(@RequestParam("appId") String appId, @RequestParam("content") String content);
}
