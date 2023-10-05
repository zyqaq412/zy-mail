package com.hzy.server.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.hzy.server.constant.LogConstant;
import com.hzy.server.mapper.MailLogMapper;
import com.hzy.server.model.dto.MailPage;
import com.hzy.server.model.entity.MailLog;
import com.hzy.server.model.vo.PageVo;
import com.hzy.server.service.MailLogService;
import com.hzy.server.utils.Result;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Date;
import java.util.List;

/**
 * (MailLog)表服务实现类
 *
 * @author makejava
 * @since 2023-10-05 21:26:38
 */
@Service()
public class MailLogServiceImpl extends ServiceImpl<MailLogMapper, MailLog> implements MailLogService {

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
