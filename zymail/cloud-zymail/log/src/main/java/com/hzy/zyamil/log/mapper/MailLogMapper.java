package com.hzy.zyamil.log.mapper;


import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.hzy.zyamil.log.model.MailLog;
import org.apache.ibatis.annotations.Mapper;


/**
 * (MailLog)表数据库访问层
 *
 * @author makejava
 * @since 2023-10-05 21:26:35
 */
@Mapper
public interface MailLogMapper extends BaseMapper<MailLog> {

}
