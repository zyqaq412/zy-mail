package com.hzy.zyamil.server.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.hzy.zyamil.common.model.dto.MailPage;
import com.hzy.zyamil.common.model.entity.Mail;
import com.hzy.zyamil.common.utils.Result;




/**
 * (Mail)表服务接口
 *
 * @author makejava
 * @since 2023-09-24 19:42:16
 */
public interface MailService extends IService<Mail> {
    /**
     *  分页获取邮件列表
     * @param mailPage
     * @return
     */
    Result getList(MailPage mailPage);

    /**
     *  根据id查看邮件详情
     * @param id
     * @return
     */
    Result getMailById(Long id);
}

