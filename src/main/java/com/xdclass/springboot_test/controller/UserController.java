package com.xdclass.springboot_test.controller;
/*
 * @Author: tianer
 * @Description: userControoler
 * @CreateTime: 14:47 2019-05-29
 */

import com.xdclass.springboot_test.domain.User;
import com.xdclass.springboot_test.service.UserService;
import com.xdclass.springboot_test.task.AsyncTask;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.Date;
import java.util.concurrent.Future;

@RestController
@RequestMapping(value = "/api/v1")
@Slf4j
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

    @Autowired
    private AsyncTask task;
    @GetMapping(value = "/task")
    public Object exeTask() throws Exception {
        long begin = System.currentTimeMillis();
//        task.task1();
//        task.task2();
//        task.task3();
        Future<String> task4 = task.task4();
        Future<String> task5 = task.task5();
        Future<String> task6 = task.task6();

        for (;;) {
            if (task4.isDone() && task5.isDone() && task6.isDone()) {
                break;
            }
        }
        long end = System.currentTimeMillis();
        long total = end - begin;
        System.out.println("完成：" + total);
        return total;
    }

    @GetMapping(value = "/log")
    public Object testLog() {
        log.debug("this is debug");
        log.info("this is info");
        log.warn("this is warn");
        log.error("this is error");
        return "ok";
    }

}
