package com.hzy.server.annotion;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * @title: SystemLog 自定义注解 用作aop切点
 * @Author zxwyhzy
 * @Date: 2023/9/23 21:54
 * @Version 1.0
 */
@Retention(RetentionPolicy.RUNTIME)
@Target(ElementType.METHOD)
public @interface SystemLog {
    String value();
}
