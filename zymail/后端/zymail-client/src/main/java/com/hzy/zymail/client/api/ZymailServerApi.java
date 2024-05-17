package com.hzy.zymail.client.api;

import com.alibaba.fastjson.JSONObject;
import com.hzy.zymail.client.config.ConfigProperties;
import com.hzy.zymail.client.constant.AppHttpCodeEnum;
import com.hzy.zymail.client.constant.SystemException;
import com.hzy.zymail.client.model.entity.Mail;
import com.hzy.zymail.client.model.entity.Source;
import com.hzy.zymail.client.utils.HttpClientUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

/**
 * @title: ZymailServerApi
 * @Author zxwyhzy
 * @Date: 2023/9/23 23:24
 * @Version 1.0
 */
@Component
public class ZymailServerApi {
    @Autowired
    private HttpClientUtils httpClientUtils;
    @Autowired
    private ConfigProperties configProperties;
    public void heartCheck() {
        try {
            httpClientUtils.get(configProperties.getServer().getUrl() + "/heart");
        } catch (Exception e) {
            throw new SystemException(AppHttpCodeEnum.SERVER_HEART_CHECK_FAILED);
        }
    }

    public void addSource(Source source) {
        try {
            heartCheck();
            httpClientUtils.put(configProperties.getServer().getUrl()+ "/sources",
                    JSONObject.toJSONString(source));
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }
    public void sendMail(Mail mail) {
        try {
            heartCheck();
            httpClientUtils.post(configProperties.getServer().getUrl()+ "/mails",
                    JSONObject.toJSONString(mail));
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }
}
