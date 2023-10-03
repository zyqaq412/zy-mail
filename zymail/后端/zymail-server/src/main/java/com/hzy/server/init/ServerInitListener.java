package com.hzy.server.init;

import com.hzy.server.service.SourceService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationListener;
import org.springframework.context.event.ContextRefreshedEvent;
import org.springframework.stereotype.Component;

/**
 * @title: ZymailInitListener
 * @Author zxwyhzy
 * @Date: 2023/10/3 17:46
 * @Version 1.0
 */
@Component
@Slf4j
public class ServerInitListener implements ApplicationListener<ContextRefreshedEvent> {
    @Autowired
    private SourceService sourceService;

    @Override
    public void onApplicationEvent(ContextRefreshedEvent event) {
        sourceService.sourcesConnectionCheck();
    }


}
