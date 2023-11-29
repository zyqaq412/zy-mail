package com.hzy.zyamil.log.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.hzy.zyamil.common.constant.LogConstant;
import com.hzy.zyamil.common.model.dto.MailPage;
import com.hzy.zyamil.common.model.vo.PageVo;
import com.hzy.zyamil.common.utils.Result;
import com.hzy.zyamil.log.mapper.MailLogMapper;
import com.hzy.zyamil.log.model.MailLog;
import com.hzy.zyamil.log.service.LogService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Date;
import java.util.List;

/**
 * @title: LogServiceImpl
 * @Author zxwyhzy
 * @Date: 2023/11/29 20:17
 * @Version 1.0
 */
@Service
public class LogServiceImpl extends ServiceImpl<MailLogMapper, MailLog> implements LogService {

    @Override
    public Result getAllLogs() {
        List<MailLog> list = list();
        return Result.okResult(list);
    }

    @Override
    public Result getLogsByAppId(String appId) {
        // LambdaQueryWrapper<MailLog> queryWrapper = new LambdaQueryWrapper<>();
        LambdaQueryWrapper<MailLog> queryWrapper = Wrappers.<MailLog>lambdaQuery();
        LambdaQueryWrapper<MailLog> list = queryWrapper.eq(MailLog::getSource, appId);
        return Result.okResult(list);
    }

    @Override
    public void log(String appId, String content, Integer level) {
        MailLog log = new MailLog();
        log.setSource(appId);
        log.setLevel(level);
        log.setContent(content);
        log.setCreateTime(new Date());
        save(log);
    }

    @Override
    public void error(String appId, String content) {
        log(appId,content, LogConstant.ERROR_LEVEL);
    }

    @Override
    public void warning(String appId, String content) {
        log(appId,content, LogConstant.WARNING_LEVEL);
    }

    @Override
    public void info(String appId, String content) {
        log(appId,content, LogConstant.INFO_LEVEL);
    }

    @Autowired
    private MailLogMapper logMapper;
    @Override
    public Result getPageLogs(MailPage mailPage, String appId) {
        Page<MailLog> page = new Page<>(mailPage.getCurrentPage(),(mailPage.getSize()));
        LambdaQueryWrapper<MailLog> queryWrapper = Wrappers.<MailLog>lambdaQuery();
        if (appId != null){
            queryWrapper.eq(MailLog::getSource,appId);
        }
        page = logMapper.selectPage(page, queryWrapper);
        List<MailLog> records = page.getRecords();
        PageVo pageVo = new PageVo();
        pageVo.setRows(records);
        pageVo.setTotal(page.getTotal());
        return Result.okResult(pageVo);
    }
}
