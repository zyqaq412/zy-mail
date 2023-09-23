package com.hzy.zymail.client.utils;

import org.apache.http.HttpResponse;
import org.apache.http.client.methods.*;
import org.apache.http.entity.ContentType;
import org.apache.http.entity.StringEntity;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.util.EntityUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.nio.charset.StandardCharsets;

/**
 * @title: HttpClientUtils
 * @Author zxwyhzy
 * @Date: 2023/9/23 20:17
 * @Version 1.0
 */

@Service
public class HttpClientUtils {
    @Autowired
    private CloseableHttpClient httpClient;

    public String get(String url) throws Exception {
        HttpGet httpGet = new HttpGet(url);
        try (CloseableHttpResponse response = httpClient.execute(httpGet)) {
            return getResponseStr(response);
        }
    }

    /**
     *
     * @param url
     * @param data 数据用json传送
     * @return
     * @throws Exception
     */
    public String post(String url,String data) throws Exception {
        HttpPost httpPost = new HttpPost(url);
        httpPost.setEntity(new StringEntity(data, ContentType.APPLICATION_JSON));
        try (CloseableHttpResponse response = httpClient.execute(httpPost)) {
            return getResponseStr(response);
        }
    }
    public String put(String url,String data) throws Exception {
        HttpPut httpPut = new HttpPut(url);
        httpPut.setEntity(new StringEntity(data, ContentType.APPLICATION_JSON));
        try (CloseableHttpResponse response = httpClient.execute(httpPut)) {
            return getResponseStr(response);
        }
    }
    public String delete(String url) throws Exception {
        HttpDelete httpDelete = new HttpDelete(url);
        try (CloseableHttpResponse response = httpClient.execute(httpDelete)) {
            return getResponseStr(response);
        }
    }

    /**
     * 获取响应字符串
     * @param res
     * @return
     * @throws IOException
     */
    private static String getResponseStr(HttpResponse res) throws IOException {
        return EntityUtils.toString(res.getEntity(), StandardCharsets.UTF_8);
    }
}
