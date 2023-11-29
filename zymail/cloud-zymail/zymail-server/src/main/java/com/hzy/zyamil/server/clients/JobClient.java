package com.hzy.zyamil.server.clients;

import com.hzy.zyamil.common.model.entity.Mail;
import com.hzy.zyamil.common.utils.Result;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Component;
import org.springframework.web.bind.annotation.PostMapping;

/**
 * @title: JobClient
 * @Author zxwyhzy
 * @Date: 2023/11/29 16:48
 * @Version 1.0
 */
@FeignClient("job-server")
@Component
public interface JobClient {
    @PostMapping(value = "/jobs/add",consumes = MediaType.APPLICATION_JSON_VALUE)
    Result addJob(Mail mail);
}
