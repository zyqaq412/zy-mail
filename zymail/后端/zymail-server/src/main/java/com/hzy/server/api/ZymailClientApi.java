package com.hzy.server.api;

import com.alibaba.fastjson.JSONObject;
import com.hzy.server.constant.RedisKeyConstant;
import com.hzy.server.model.entity.Mail;
import com.hzy.server.model.entity.Source;
import com.hzy.server.utils.RedisCache;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import com.hzy.server.utils.HttpClientUtils;

import java.io.IOException;

/**
 * @title: ZymailClientApi
 * @Author zxwyhzy
 * @Date: 2023/9/24 17:34
 * @Version 1.0
 */
@Component
public class ZymailClientApi {
    @Autowired
    private RedisCache redisCache;
    @Autowired
    private HttpClientUtils httpClientUtils;

    public void sendEmail(Mail mail) throws IOException {

        try {
            Source source = redisCache.getCacheMapValue(RedisKeyConstant.SOURCES_KEY,
                    mail.getSource());
            httpClientUtils.post(source.getUrl() + "/mails", JSONObject.toJSONString(mail));
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }
}
