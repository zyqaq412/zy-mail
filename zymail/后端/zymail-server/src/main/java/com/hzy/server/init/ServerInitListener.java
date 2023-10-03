package com.hzy.server.init;

import com.hzy.server.api.ZymailClientApi;
import com.hzy.server.constant.SystemConstant;
import com.hzy.server.utils.RedisCache;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationListener;
import org.springframework.context.event.ContextRefreshedEvent;
import org.springframework.stereotype.Component;

import java.util.Map;

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
    private RedisCache redisCache;
    @Autowired
    private ZymailClientApi zymailClientApi;

    @Override
    public void onApplicationEvent(ContextRefreshedEvent event) {
        sourcesConnectionCheck();
    }
    public void sourcesConnectionCheck(){
        log.info("服务器启动初始化，检查缓存中的调度源存活状态");
        Map<String, Object> sources = redisCache.getHashAll(SystemConstant.SOURCES_KEY);
        for (Map.Entry<String, Object> source : sources.entrySet()){
            zymailClientApi.heartCheck(source.getKey());
        }
    }

}
