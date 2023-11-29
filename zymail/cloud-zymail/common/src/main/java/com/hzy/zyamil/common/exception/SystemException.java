package com.hzy.zyamil.common.exception;

import com.hzy.zyamil.common.utils.CodeEnum;

/**
 * @title: SystemException
 * @Author zxwyhzy
 * @Date: 2023/11/29 19:39
 * @Version 1.0
 */
public class SystemException extends RuntimeException{

    private int code;

    private String msg;

    public int getCode() {
        return code;
    }

    public String getMsg() {
        return msg;
    }

    public SystemException(CodeEnum httpCodeEnum) {
        super(httpCodeEnum.getMsg());
        this.code = httpCodeEnum.getCode();
        this.msg = httpCodeEnum.getMsg();
    }
    public SystemException(CodeEnum httpCodeEnum,String msg) {
        super(httpCodeEnum.getMsg());
        this.code = httpCodeEnum.getCode();
        this.msg = msg;
    }
}

