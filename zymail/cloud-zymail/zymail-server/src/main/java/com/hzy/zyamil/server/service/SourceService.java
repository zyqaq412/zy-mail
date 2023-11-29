package com.hzy.zyamil.server.service;

import com.hzy.zyamil.common.utils.Result;
import com.hzy.zyamil.server.model.entity.Source;


/**
 * @title: SourceService
 * @Author zxwyhzy
 * @Date: 2023/9/23 23:12
 * @Version 1.0
 */
public interface SourceService {

    /**
     * 添加调度源
     * @param source
     * @return
     */
    void addSource(Source source);

    /**
     *  获取调度源
     * @return
     */
    Result getList();

    /**
     *  检测调度源连接状态
     */
    void sourcesConnectionCheck();
}