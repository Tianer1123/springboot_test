package com.xdclass.springboot_test.service;
/*
 * @Author: tianer
 * @Description: UserService
 * @CreateTime: 14:06 2019-05-29
 */

import com.xdclass.springboot_test.domain.User;

public interface UserService {
    int add(User user);

    /**
     * 测试事务
     * @return id值
     */
    User testTraction();
}
