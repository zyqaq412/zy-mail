package com.hzy.zyamil.server.aop;

import com.hzy.zyamil.common.annotion.SystemLog;
import lombok.extern.slf4j.Slf4j;
import org.aspectj.lang.JoinPoint;
import org.aspectj.lang.annotation.AfterReturning;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Before;
import org.aspectj.lang.annotation.Pointcut;
import org.aspectj.lang.reflect.MethodSignature;
import org.springframework.stereotype.Component;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;

import javax.servlet.http.HttpServletRequest;
import java.util.Enumeration;

/**
 * @title: LoggerAop
 * @Author zxwyhzy
 * @Date: 2023/9/23 21:56
 * @Version 1.0
 */
@Component // 注入容器
@Aspect // 告诉spring容器这是一个切面类
@Slf4j
public class LoggerAop {
    @Pointcut("@annotation(com.hzy.zyamil.common.annotion.SystemLog)")
    public void controllerLog() {
    }

    @Before("controllerLog()")
    public void doBefore(JoinPoint joinPoint) {
        ServletRequestAttributes attributes = (ServletRequestAttributes) RequestContextHolder.getRequestAttributes();
        HttpServletRequest httpServletRequest = attributes.getRequest();
        // 获取被增强方法上的注解对象
        SystemLog systemLog = getSystemLog(joinPoint);
        log.info("接口描述   : {}", systemLog.value());
        log.info("URL : " + httpServletRequest.getRequestURL().toString());
        log.info("HTTP_METHOD : " + httpServletRequest.getMethod());
        log.info("IP : " + httpServletRequest.getRemoteAddr());
        Enumeration<String> enu = httpServletRequest.getParameterNames();
        while (enu.hasMoreElements()) {
            String name = enu.nextElement();
            log.info("name : " + name + ", value : " + httpServletRequest.getParameter(name));
        }
    }

    @AfterReturning(returning = "ret", pointcut = "controllerLog()")
    public void doAfterReturning(Object ret) {
        log.info("RESPONSE : " + ret);
    }
    private SystemLog getSystemLog(JoinPoint joinPoint) {
        MethodSignature signature = (MethodSignature) joinPoint.getSignature();
        return signature.getMethod().getAnnotation(SystemLog.class);
    }
}
