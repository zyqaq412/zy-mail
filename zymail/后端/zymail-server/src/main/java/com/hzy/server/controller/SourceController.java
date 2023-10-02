package com.hzy.server.controller;

import com.hzy.server.annotion.SystemLog;
import com.hzy.server.model.entity.Source;
import com.hzy.server.service.SourceService;
import com.hzy.server.utils.Result;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;

@RequestMapping("/sources")
@RestController
public class SourceController {

    @Resource
    private SourceService sourceService;


    /**
     * 客户端接入，添加调度源（远程调用）
     */
    @SystemLog("添加调度源")
    @PutMapping
    public Result addSource(@RequestBody Source source) {
        sourceService.addSource(source);
        return Result.okResult();
    }
    @SystemLog("获取调度源列表")
    @GetMapping
    public Result getSources(){
      return sourceService.getList();
    }

}