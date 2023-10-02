package com.hzy.server.service.impl;

import com.hzy.server.config.ConfigProperties;
import com.hzy.server.constant.SystemConstant;
import com.hzy.server.model.entity.Source;
import com.hzy.server.service.SourceService;
import com.hzy.server.utils.Result;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import com.hzy.server.utils.RedisCache;
import org.springframework.stereotype.Service;

import java.util.Collection;

/**
 * @title: SourceService
 * @Author zxwyhzy
 * @Date: 2023/9/23 23:13
 * @Version 1.0
 */
@Service
@Slf4j
public class SourceServiceImpl implements SourceService {

    @Autowired
    private RedisCache redisCache;
    @Override
    public void addSource(Source source) {
        if (StringUtils.isNotBlank(source.getAppId()) &&
                StringUtils.isNotBlank(source.getHost()) &&
                StringUtils.isNotBlank(source.getUsername()) &&
                StringUtils.isNotBlank(source.getUrl())) {
            String key = source.getAppId();
            redisCache.setCacheMapValue(SystemConstant.SOURCES_KEY,key,source);
            log.info(key+"接入成功");
        }
    }

    @Autowired
    private ConfigProperties configProperties;
    @Override
    public Result getList() {
        Collection<String> hashHkeys = redisCache.getHashHkeys(SystemConstant.SOURCES_KEY);
        // 添加本地源
        hashHkeys.add(configProperties.getAppId());
        return Result.okResult(hashHkeys);
    }
}
