package com.hzy.server.service.impl;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.hzy.server.mapper.MailMapper;
import com.hzy.server.model.dto.MailPage;
import com.hzy.server.model.entity.Mail;
import com.hzy.server.model.vo.PageVo;
import com.hzy.server.service.MailService;
import com.hzy.server.utils.Result;
import org.springframework.stereotype.Service;

import java.util.List;

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
}
