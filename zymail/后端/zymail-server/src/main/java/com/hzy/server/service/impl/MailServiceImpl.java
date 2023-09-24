package com.hzy.server.service.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.hzy.server.mapper.MailMapper;
import com.hzy.server.model.entity.Mail;
import com.hzy.server.service.MailService;
import org.springframework.stereotype.Service;

/**
 * (Mail)表服务实现类
 *
 * @author makejava
 * @since 2023-09-24 19:42:16
 */
@Service
public class MailServiceImpl extends ServiceImpl<MailMapper, Mail> implements MailService {

}
