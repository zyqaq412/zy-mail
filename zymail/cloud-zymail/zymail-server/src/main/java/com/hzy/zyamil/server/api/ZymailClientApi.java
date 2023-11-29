package com.hzy.zyamil.server.api;

import com.alibaba.fastjson.JSONObject;
import com.hzy.zyamil.common.constant.SystemConstant;
import com.hzy.zyamil.common.exception.SystemException;
import com.hzy.zyamil.common.model.entity.Mail;
import com.hzy.zyamil.common.utils.CodeEnum;
import com.hzy.zyamil.server.model.entity.Source;
import com.hzy.zyamil.server.utils.HttpClientUtils;
import com.hzy.zyamil.server.utils.RedisCache;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.io.IOException;

/**
 * @title: ZymailClientApi
 * @Author zxwyhzy
 * @Date: 2023/9/24 17:34
 * @Version 1.0
 */
@Component
@Slf4j
public class ZymailClientApi {
    @Autowired
    private RedisCache redisCache;
    @Autowired
    private HttpClientUtils httpClientUtils;

    public void sendEmail(Mail mail) throws IOException {
        try {
            heartCheck(mail.getSource());
            Source source = redisCache.getCacheMapValue(SystemConstant.SOURCES_KEY,
                    mail.getSource());
            httpClientUtils.post(source.getUrl() + "/mails", JSONObject.toJSONString(mail));
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }
    public void heartCheck(String appId) {
        try {
            Source source = redisCache.getCacheMapValue(SystemConstant.SOURCES_KEY,
                    appId);
            httpClientUtils.get(source.getUrl() + "/heart");
        } catch (Exception e) {
            redisCache.delCacheMapValue(SystemConstant.SOURCES_KEY,appId);
            log.info("移除调度源:"+appId);
            throw new SystemException(CodeEnum.CLIENT_HEART_CHECK_FAILED);
        }
    }
}
