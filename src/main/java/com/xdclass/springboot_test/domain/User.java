package com.xdclass.springboot_test.domain;
/*
 * @Author: tianer1123
 * @Description: User
 * @CreateTime: 09:18 2019-05-28
 */


import lombok.Data;

import java.util.Date;

@Data
public class User {
    private int id;
    private String name;
    private String phone;
    private int age;
    private Date createTime;
}
