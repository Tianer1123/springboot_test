package com.xdclass.springboot_test.controller;
/*
 * @Author: tianer
 * @Description: userControoler
 * @CreateTime: 14:47 2019-05-29
 */

import com.alibaba.fastjson.JSON;
import com.xdclass.springboot_test.domain.User;
import com.xdclass.springboot_test.service.UserService;
import com.xdclass.springboot_test.task.AsyncTask;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.*;
import java.util.concurrent.Future;
import java.util.function.Function;
import java.util.stream.Collectors;


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

    @GetMapping(value = "/stream")
    public Object testStream() {
        User u1 = new User(1, "张三", "10010", 23, new Date());
        User u2 = new User(2, "李四", "10011", 24, new Date());
        User u3 = new User(3, "王五", "10012", 25, new Date());
        User u4 = new User(4, "赵六", "10013", 26, new Date());
        User u5 = new User(5, "周七", "10014", 27, new Date());
        User u6 = new User(6, "郑八", "10015", 28, new Date());

        List<User> userList = new ArrayList<>();
        userList.add(u1);
        userList.add(u2);
        userList.add(u3);
        userList.add(u4);
        userList.add(u5);
        userList.add(u6);

        // 获取ids列表
        List<String> ids = userList.stream().map(user -> String.valueOf(user.getId())).collect(Collectors.toList());
        // 获取 id:name hashMap
        Map<Integer, String> map = userList.stream().collect(Collectors.toMap(User::getId, User::getName));
        // key 为 id, value 为 user 对象
        Map<Integer, User> userMap = userList.stream().collect(Collectors.toMap(User::getId, user -> user));
        // list中删除id为1的对象
        userList.removeIf(user -> String.valueOf(user.getId()).equals("1"));
        // 根据电话重新排序
        Collections.sort(userList, (o1, o2) -> {
            if (o1.getPhone().compareTo(o2.getPhone()) > 0) {
                return 1;
            } else if (o1.getPhone().compareTo(o2.getPhone()) < 0) {
                return -1;
            } else {
                return 0;
            }
        });
        // userList根据某个字段过滤排序
        Map<String, User> filterMap = userList.stream().collect(Collectors.toMap(user -> "李四".equals(user.getName()) ? user.getPhone() : user.getName(), user -> user));

        return filterMap;
    }

}
