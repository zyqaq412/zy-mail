package com.hzy.zyamil.job.init;

import com.hzy.zyamil.job.service.JobService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationListener;
import org.springframework.context.annotation.Lazy;
import org.springframework.context.event.ContextRefreshedEvent;
import org.springframework.stereotype.Component;

/**
 * @title: JobInitListenner
 * @Author zxwyhzy
 * @Date: 2023/11/29 22:30
 * @Version 1.0
 */
@Component
@Slf4j
public class JobInitListener implements ApplicationListener<ContextRefreshedEvent> {

    @Autowired
    @Lazy
    private JobService jobService;

    static int count = 0;

    @Override
    public void onApplicationEvent(ContextRefreshedEvent event) {
        count++;
        if (count > 1) return;
        // TODO 这里会调用两次
        log.info("job服务启动初始化，重载任务");
        log.info("Received ContextRefreshedEvent. Source: {}", event.getSource());
        jobService.JobHeavyLoad();

    }


}
