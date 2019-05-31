package com.xdclass.springboot_test.service.impl;
/*
 * @Author:
 * @Description: TODO
 * @CreateTime: 14:08 2019-05-29
 */

import com.xdclass.springboot_test.domain.User;
import com.xdclass.springboot_test.mapper.UserMapper;
import com.xdclass.springboot_test.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

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
}
