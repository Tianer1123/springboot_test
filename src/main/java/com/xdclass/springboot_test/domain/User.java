package com.xdclass.springboot_test.domain;
/*
 * @Author: tianer1123
 * @Description: User
 * @CreateTime: 09:18 2019-05-28
 */


import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.format.annotation.DateTimeFormat;

import java.util.Date;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class User {
    private int id;
    private String name;
    private String phone;
    private int age;
    @DateTimeFormat(pattern = "yyyy-MM-dd")
    private Date createTime;
}
