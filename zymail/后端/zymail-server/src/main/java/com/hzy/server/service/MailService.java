package com.hzy.server.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.hzy.server.model.dto.MailPage;
import com.hzy.server.model.entity.Mail;
import com.hzy.server.utils.Result;


/**
 * (Mail)表服务接口
 *
 * @author makejava
 * @since 2023-09-24 19:42:16
 */
public interface MailService extends IService<Mail> {
    Result getList(MailPage mailPage);
}

