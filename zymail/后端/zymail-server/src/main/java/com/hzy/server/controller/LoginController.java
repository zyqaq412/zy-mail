package com.hzy.server.controller;

import com.hzy.server.config.ConfigProperties;
import com.hzy.server.model.dto.LoginDto;
import com.hzy.server.utils.Result;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.Map;

/**
 * @title: LoginController
 * @Author zxwyhzy
 * @Date: 2023/9/30 22:57
 * @Version 1.0
 */
@RequestMapping
@RestController

public class LoginController {
    @Autowired
    private ConfigProperties configProperties;

    @PostMapping("/login")
    public Result login(@RequestBody LoginDto user){
        if (user.getPassword().equals(configProperties.getAdmin().getPassword()) &&
         user.getUsername().equals(configProperties.getAdmin().getUsername())){
            Map<String, Object> map = new HashMap<>();
            map.put("token", "admin");
            return Result.okResult(map);
        }
        return Result.errorResult(405,"用户名或密码错误");
    }
    /**
     * 获取用户信息
     * @return
     */
    @GetMapping("info")
    public Result info() {
        Map<String, Object> map = new HashMap<>();
        map.put("roles","[admin]");
        map.put("name","admin");
        map.put("avatar","https://cdn.jsdelivr.net/gh/zxwyhzy/zy-img1/md/202311091226366.png");
        return Result.okResult(map);
    }
    /**
     * 退出
     * @return
     */
    @PostMapping("logout")
    public Result logout(){
        return Result.okResult();
    }
}
