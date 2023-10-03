package com.hzy.zymail.client.constant;

/**
 * @title: SystemException 自定义异常
 * @Author zxwyhzy
 * @Date: 2023/9/23 21:52
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

    public SystemException(AppHttpCodeEnum httpCodeEnum) {
        super(httpCodeEnum.getMsg());
        this.code = httpCodeEnum.getCode();
        this.msg = httpCodeEnum.getMsg();
    }

}
