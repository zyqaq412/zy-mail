package com.hzy.server.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.hzy.server.model.entity.Mail;
import org.apache.ibatis.annotations.Mapper;


/**
 * (Mail)表数据库访问层
 *
 * @author makejava
 * @since 2023-09-24 19:42:14
 */
@Mapper
public interface MailMapper extends BaseMapper<Mail> {

}
