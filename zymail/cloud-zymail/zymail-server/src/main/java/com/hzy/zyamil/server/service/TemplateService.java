package com.hzy.zyamil.server.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.hzy.zyamil.common.utils.Result;
import com.hzy.zyamil.server.model.entity.Template;



/**
 * (Template)表服务接口
 *
 * @author makejava
 * @since 2023-10-04 23:38:04
 */
public interface TemplateService extends IService<Template> {


    /**
     *  获取模板
     * @return
     */
    Result getTemplates();
}

