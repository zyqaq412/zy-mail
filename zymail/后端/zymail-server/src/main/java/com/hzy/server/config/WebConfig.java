package com.hzy.server.config;

import com.hzy.server.filter.JobsInterceptor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.CorsRegistry;
import org.springframework.web.servlet.config.annotation.InterceptorRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

/**
 * @title: WebConfig
 * @Author zxwyhzy
 * @Date: 2023/10/6 17:25
 * @Version 1.0
 */
@Configuration
public class WebConfig implements WebMvcConfigurer {
    @Override
    public void addCorsMappings(CorsRegistry registry) {
        // 设置允许跨域的路径
        registry.addMapping("/**")
                // 设置允许跨域请求的域名
                .allowedOriginPatterns("*")
                // 是否允许cookie
                .allowCredentials(true)
                // 设置允许的请求方式
                .allowedMethods("GET", "POST", "DELETE", "PUT")
                // 设置允许的header属性
                // .allowedHeaders("*")
                .allowedHeaders("application/json")
                // 跨域允许时间
                .maxAge(3600);
    }
    @Autowired
    private JobsInterceptor jobsInterceptor;
    @Override
    public void addInterceptors(InterceptorRegistry registry) {

        registry.addInterceptor(jobsInterceptor)
                .addPathPatterns("/jobs/**") ;// 指定要拦截的路径
    }
}
