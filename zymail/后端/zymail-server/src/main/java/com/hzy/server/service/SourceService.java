package com.hzy.server.service;
import com.hzy.server.model.entity.Source;
import com.hzy.server.utils.Result;

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
}