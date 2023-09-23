package com.hzy.zymail.client.init;

import com.hzy.zymail.client.utils.HttpClientUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationListener;
import org.springframework.context.event.ContextRefreshedEvent;
import org.springframework.stereotype.Component;

/**
 * @title: ZymailInitListener
 * @Author zxwyhzy
 * @Date: 2023/9/23 18:07
 * @Version 1.0
 * <p>
 * ApplicationListener<ContextRefreshedEvent>
 * 是 Spring Framework 中的一个接口，
 * 用于监听应用程序上下文（ApplicationContext）
 * 刷新事件（ContextRefreshedEvent）。
 * <p>
 * 当应用程序启动时，Spring容器会加载并初始化ApplicationContext，
 * 这包括实例化和初始化所有的bean，然后触发一系列的应用程序事件。
 * 其中之一就是ContextRefreshedEvent，它表示ApplicationContext已经成功地被刷新并准备好处理请求。
 */
@Component
public class ZymailInitListener implements ApplicationListener<ContextRefreshedEvent> {

    @Autowired
    private HttpClientUtils httpClientUtils;

    // 这个监听器中编写自定义的业务逻辑，以便在应用程序启动时或ApplicationContext被刷新时执行一些初始化工作。
    // 在这里编写与zymail-server连接的代码
    @Override
    public void onApplicationEvent(ContextRefreshedEvent event) {
        // 在应用程序启动或ApplicationContext被刷新时执行的代码
        System.out.println("ApplicationContext已刷新，可以执行初始化工作。");
        try {
            String string = httpClientUtils.get("http://localhost:8888/test");
            System.out.println(string);
        } catch (Exception e) {
            throw new RuntimeException(e);
        }

    }
}