package com.hzy.zyamil.common.model.dto;

import lombok.Data;

/**
 * @title: JobDto
 * @Author zxwyhzy
 * @Date: 2023/10/6 17:14
 * @Version 1.0
 */
@Data
public class JobDto {
    private String jobName;
    private String jobGroupName;
    private String startTime;
    private String endTime;
    private String cron;
}
