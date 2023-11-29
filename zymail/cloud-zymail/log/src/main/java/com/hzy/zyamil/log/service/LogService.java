package com.hzy.zyamil.log.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.hzy.zyamil.common.model.dto.MailPage;
import com.hzy.zyamil.common.utils.Result;
import com.hzy.zyamil.log.model.MailLog;

/**
 * @title: LogService
 * @Author zxwyhzy
 * @Date: 2023/11/29 20:17
 * @Version 1.0
 */
public interface LogService extends IService<MailLog> {

    /**
     * 获取所有日志
     *
     * @return
     */
    Result getAllLogs();

    /**
     * 获取指定调度源日志
     *
     * @param appId
     * @return
     */
    Result getLogsByAppId(String appId);

    // region 日志记录

    /**
     * 日志记录
     *
     * @param appId   调度源
     * @param content 内容
     * @param level   等级
     */
    void log(String appId, String content, Integer level);

    void error(String appId, String content);

    void warning(String appId, String content);

    void info(String appId, String content);

    /**
     *  分页查询
     * @param mailPage
     * @param appId
     * @return
     */
    Result getPageLogs(MailPage mailPage, String appId);
    // endregion


}


