package com.hzy.server.service.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.hzy.server.mapper.TemplateMapper;
import com.hzy.server.model.entity.Template;
import com.hzy.server.service.TemplateService;
import com.hzy.server.utils.Result;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * (Template)表服务实现类
 *
 * @author makejava
 * @since 2023-10-04 23:38:04
 */
@Service()
public class TemplateServiceImpl extends ServiceImpl<TemplateMapper, Template> implements TemplateService {

    @Override
    public Result getTemplates() {
        List<Template> list = list();
        return Result.okResult(list);
    }
}
