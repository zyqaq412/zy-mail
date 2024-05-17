package com.hzy.zyamil.server.aop;


import com.hzy.zyamil.common.exception.SystemException;
import com.hzy.zyamil.common.utils.Result;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

/**
 * @title: GlobalExceptionHandler
 * @Author zxwyhzy
 * @Date: 2023/9/23 21:56
 * @Version 1.0
 */
@RestControllerAdvice
@Slf4j
public class GlobalExceptionHandler {

    @ExceptionHandler(SystemException.class)
    public Result businessExceptionHandler(SystemException ex) {
        ex.printStackTrace();
        log.error("业务异常 -- 错误码：" + ex.getCode() + " 错误信息：" + ex.getMessage());
        return Result.errorResult(ex.getCode(), ex.getMessage());
    }
    @ExceptionHandler(RuntimeException.class)
    public Result runtimeExceptionHandler(Exception ex) {
        log.error("运行时异常 -- 错误信息："  );
        ex.printStackTrace();
        return Result.errorResult(500, ex.getMessage());
    }

}
