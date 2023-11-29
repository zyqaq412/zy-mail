package com.hzy.zyamil.server.service.impl;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.hzy.zyamil.common.constant.SystemConstant;
import com.hzy.zyamil.common.model.dto.MailPage;
import com.hzy.zyamil.common.model.entity.Mail;
import com.hzy.zyamil.common.model.vo.PageVo;
import com.hzy.zyamil.common.utils.Result;
import com.hzy.zyamil.server.mapper.MailMapper;
import com.hzy.zyamil.server.service.MailService;
import com.hzy.zyamil.server.utils.RedisCache;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.concurrent.TimeUnit;

/**
 * (Mail)表服务实现类
 *
 * @author makejava
 * @since 2023-09-24 19:42:16
 */
@Service
public class MailServiceImpl extends ServiceImpl<MailMapper, Mail> implements MailService {

    @Override
    public Result getList(MailPage mailPage) {
        Page<Mail> page = new Page<>();
        page.setCurrent(mailPage.getCurrentPage());
        page.setSize(mailPage.getSize());
        page(page);
        List<Mail> records = page.getRecords();
        PageVo pageVo = new PageVo();
        pageVo.setRows(records);
        pageVo.setTotal(page.getTotal());
        return Result.okResult(pageVo);
    }

    @Autowired
    private RedisCache redisCache;

    @Override
    public Result getMailById(Long id) {
        // 先查缓存
        Mail mail = (Mail) redisCache.getCacheObject(SystemConstant.MAIL_KEY + id);
        if (mail == null) {
            // 查数据库
            mail = getById(id);
            // 创建缓存
            redisCache.setCacheObject(SystemConstant.MAIL_KEY + id, mail,
                    SystemConstant.CACHE_EXPIRE_TIME, TimeUnit.MINUTES);
            return Result.okResult(mail);
        }
        return Result.okResult(mail);
    }
}
