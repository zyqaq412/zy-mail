package com.hzy.server.model.entity;

import lombok.Data;

import java.io.Serializable;
import java.util.Date;

/**
 * @title: Job
 * @Author zxwyhzy
 * @Date: 2023/10/4 17:24
 * @Version 1.0
 */
@Data
public class JobVo implements Serializable {
    /**
     * 邮件信息
     */
    private Mail mail;



    /**
     * 状态
     */
    private Integer state;

    /**
     * 工作名
     */
    private String jobName;

    /**
     * 工作组名
     */
    private String jobGroupName;

    /**
     * 开始时间
     */
    private Date startTime;

    /**
     * 结束时间
     */
    private Date endTime;

    /**
     * 上次调度时间
     */
    private Date previousFireTime;

    /**
     * 下次调度时间
     */
    private Date nextFireTime;

    /**
     *  实列 端口号
     * */
    private int port;


    private static final long serialVersionUID = -654956488863895435L;

}
