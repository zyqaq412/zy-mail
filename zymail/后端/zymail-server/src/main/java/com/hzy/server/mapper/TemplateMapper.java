package com.hzy.server.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.hzy.server.model.entity.Template;
import org.apache.ibatis.annotations.Mapper;


/**
 * (Template)表数据库访问层
 *
 * @author makejava
 * @since 2023-10-04 23:38:02
 */
@Mapper
public interface TemplateMapper extends BaseMapper<Template> {

}
