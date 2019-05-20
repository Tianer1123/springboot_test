package com.xdclass.springboot_test.domain;/*
 * @Author: tianer
 * @Description: easypoi导入导出测试bean
 * @CreateTime: 14:26 2019-05-20
 */

import cn.afterturn.easypoi.excel.annotation.Excel;

import java.io.Serializable;
import java.util.Date;

public class Person implements Serializable {
    @Excel(name = "姓名", orderNum = "0")
    private String name;
    @Excel(name = "性别", replace = {"男_1", "女_2"},orderNum = "1")
    private String sex;
    @Excel(name = "生日", exportFormat = "yyyy-MM-dd", orderNum = "2")
    private Date birthday;

    public Person(String name, String sex, Date birthday) {
        this.name = name;
        this.sex = sex;
        this.birthday = birthday;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getSex() {
        return sex;
    }

    public void setSex(String sex) {
        this.sex = sex;
    }

    public Date getBirthday() {
        return birthday;
    }

    public void setBirthday(Date birthday) {
        this.birthday = birthday;
    }
}
