package com.hzy.zymail.client.controller;

import com.hzy.zymail.client.constant.AppHttpCodeEnum;
import com.hzy.zymail.client.utils.Result;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * @title: HeartController 心跳检测
 * @Author zxwyhzy
 * @Date: 2023/10/3 17:36
 * @Version 1.0
 */
@RestController
@RequestMapping("/heart")
public class HeartController {

    /**
     * 心跳检测
     * @return
     */
    @GetMapping
    public Result<Integer> heartCheck() {
        return Result.okResult(AppHttpCodeEnum.SERVER_HEART_CHECK_SUCCESS);
    }

}
