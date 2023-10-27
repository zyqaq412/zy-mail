package com.hzy.server.service;

import com.hzy.server.utils.Result;

/**
 * @title: JobServer
 * @Author zxwyhzy
 * @Date: 2023/10/4 17:21
 * @Version 1.0
 */
public interface JobService {
    /**
     *  获取所有工作
     * @return
     */
    Result getAllJobs();

    /**
     *  根据调度源获取工作
     * @return
     */
    Result getJobsByAppId(String appId);

    /**
     *  项目重启时 加载之前未完成任务
     */
    void JobHeavyLoad();
}
