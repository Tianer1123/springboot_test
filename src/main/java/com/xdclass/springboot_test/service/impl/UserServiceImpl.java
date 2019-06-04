package com.xdclass.springboot_test.service.impl;
/*
 * @Author: tianer1123
 * @Description: 用户Service接口
 * @CreateTime: 14:08 2019-05-29
 */

import com.xdclass.springboot_test.domain.User;
import com.xdclass.springboot_test.mapper.UserMapper;
import com.xdclass.springboot_test.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Isolation;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import java.util.Date;

@Service
public class UserServiceImpl implements UserService {

    @Autowired
    private UserMapper mapper;

    @Override
    public int add(User user) {
        mapper.inseart(user);
        int id = user.getId();
        return id;
    }

    @Override
    @Transactional(propagation = Propagation.REQUIRED, isolation = Isolation.DEFAULT)
    public User testTraction() {
        User user = new User();
        user.setAge(25);
        user.setCreateTime(new Date());
        user.setName("事务测试");
        user.setPhone("110");

        mapper.inseart(user);

        int a = 19 / 0;

        return user;
    }
}
