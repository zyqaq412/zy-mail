package com.hzy.zyamil.server.service.impl;

import com.hzy.zyamil.common.constant.SystemConstant;
import com.hzy.zyamil.common.exception.SystemException;
import com.hzy.zyamil.common.utils.CodeEnum;
import com.hzy.zyamil.common.utils.Result;
import com.hzy.zyamil.server.api.ZymailClientApi;
import com.hzy.zyamil.server.config.ConfigProperties;
import com.hzy.zyamil.server.model.entity.Source;
import com.hzy.zyamil.server.service.SourceService;
import com.hzy.zyamil.server.utils.RedisCache;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
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
    @Autowired
    private ZymailClientApi zymailClientApi;
    @Override
    public Result getList() {
        sourcesConnectionCheck();
        Collection<String> hashHkeys = redisCache.getHashHkeys(SystemConstant.SOURCES_KEY);
        // 添加本地源
        hashHkeys.add(configProperties.getAppId());
        return Result.okResult(hashHkeys);
    }
    @Override
    public void sourcesConnectionCheck(){

        Collection<String> sources = redisCache.getHashHkeys(SystemConstant.SOURCES_KEY);
        for (String appId : sources){
            try {
                zymailClientApi.heartCheck(appId);
            }catch (SystemException e){
                throw new SystemException(CodeEnum.CLIENT_HEART_CHECK_FAILED);
            }
        }
    }
}
