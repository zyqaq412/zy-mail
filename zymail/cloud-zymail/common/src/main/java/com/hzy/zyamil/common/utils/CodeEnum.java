package com.hzy.zyamil.common.utils;

/**
 * @title: CodeEnum
 * @Author zxwyhzy
 * @Date: 2023/11/29 15:55
 * @Version 1.0
 */
public enum CodeEnum {
    SUCCESS(200,"操作成功"),

    SYSTEM_ERROR(500,"出现错误"),
    QUARTZ_ERROR(510, "定时任务调度失败"),
    CLIENT_HEART_CHECK_FAILED(0, "客户端心跳检测失败"),
    CLIENT_HEART_CHECK_SUCCESS(1,"客户端心跳检测成功"),
    JOB_MODIFY_PARAM_ERROR(410,"任务修改参数类别未知"),
    JOB_MODIFY_ERROR(511,"任务修改失败"),
    JOB_HEAVYLOAD_ERROR(512,"任务重载失败");



    int code;
    String msg;

    CodeEnum(int code, String errorMessage){
        this.code = code;
        this.msg = errorMessage;
    }

    public int getCode() {
        return code;
    }

    public String getMsg() {
        return msg;
    }
}
