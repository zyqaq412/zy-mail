package com.hzy.zyamil.job.jobs;

import org.quartz.Job;
import org.quartz.JobExecutionContext;
import org.quartz.JobExecutionException;
import org.springframework.stereotype.Component;

/**
 * @title: RemoteSendMailJob
 * @Author zxwyhzy
 * @Date: 2023/11/29 16:26
 * @Version 1.0
 */
@Component
public class RemoteSendMailJob implements Job {
    @Override
    public void execute(JobExecutionContext jobExecutionContext) throws JobExecutionException {
        System.out.println("执行定时任务");
    }
}
