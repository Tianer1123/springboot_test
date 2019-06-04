package com.xdclass.springboot_test.controller;
/*
 * @Author: tianer
 * @Description: userControoler
 * @CreateTime: 14:47 2019-05-29
 */

import com.xdclass.springboot_test.domain.User;
import com.xdclass.springboot_test.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.Date;

@RestController
@RequestMapping(value = "/api/v1/user")
public class UserController {
    @Autowired
    private UserService service;

    @Autowired
    private StringRedisTemplate redisTpl;

    @GetMapping(value = "/add")
    public Object add() {
        User user = new User();
        user.setAge(11);
        user.setCreateTime(new Date());
        user.setName("xdClass");
        user.setPhone("100001110");
        int id = service.add(user);
        return user;
    }

    @GetMapping(value = "/account")
    public Object account() {
        User user = service.testTraction();
        return user;
    }

    @GetMapping(value = "/redis/add")
    public Object redisAdd() {
        redisTpl.opsForValue().set("name", "lilei");
        return "OK";
    }

    @GetMapping(value = "/redis/get")
    public Object redisGet() {
        String value = redisTpl.opsForValue().get("name");
        return value;
    }

}
