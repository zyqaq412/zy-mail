package com.hzy.server.service.impl;

import com.hzy.server.constant.RedisKeyConstant;
import com.hzy.server.model.entity.Source;
import com.hzy.server.service.SourceService;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import com.hzy.server.utils.RedisCache;
import org.springframework.stereotype.Service;

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
            redisCache.setCacheMapValue(RedisKeyConstant.SOURCES_KEY,key,source);
            log.info(key+"接入成功");
        }
    }
}
