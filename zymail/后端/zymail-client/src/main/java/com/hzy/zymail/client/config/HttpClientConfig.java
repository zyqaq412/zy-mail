package com.hzy.zymail.client.config;

import org.apache.http.Header;
import org.apache.http.HttpRequest;
import org.apache.http.HttpRequestInterceptor;
import org.apache.http.client.config.RequestConfig;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClients;
import org.apache.http.impl.conn.PoolingHttpClientConnectionManager;
import org.apache.http.message.BasicHeader;
import org.apache.http.protocol.HttpContext;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

/**
 * @title: HttpClientConfig
 * @Author zxwyhzy
 * @Date: 2023/9/23 19:45
 * @Version 1.0
 */
@Configuration
public class HttpClientConfig {
    @Bean
    public PoolingHttpClientConnectionManager poolingConnectionManager() {
        PoolingHttpClientConnectionManager connectionManager = new PoolingHttpClientConnectionManager();
        connectionManager.setMaxTotal(200); // 设置最大连接数
        connectionManager.setDefaultMaxPerRoute(100); // 设置每个路由的最大连接数
        return connectionManager;
    }

    @Bean
    public RequestConfig requestConfig() {
        return RequestConfig.custom()
                .setConnectTimeout(5000) // 连接超时时间
                .setSocketTimeout(5000) // 请求超时时间
                .build();
    }

    @Bean
    public CloseableHttpClient httpClient() {
        PoolingHttpClientConnectionManager connectionManager = poolingConnectionManager();
        RequestConfig requestConfig = requestConfig();


        return HttpClients.custom()
                .setConnectionManager(connectionManager)
                .setDefaultRequestConfig(requestConfig)
                .addInterceptorFirst(new RequestDefaultHeaders())
                .build();
    }

    // 配置默认请求头
    class RequestDefaultHeaders implements HttpRequestInterceptor {
        @Override
        public void process(HttpRequest request, HttpContext context) {
            // 添加默认请求头
            Header defaultHeader = new BasicHeader("Content-Type", "application/json");
            request.addHeader(defaultHeader);
        }
    }

}
