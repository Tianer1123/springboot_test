package com.xdclass.springboot_test.controller;/*
 * @Author: tianer
 * @Description: 导出Excel接口
 * @CreateTime: 14:33 2019-05-20
 */

import com.xdclass.springboot_test.domain.Person;
import com.xdclass.springboot_test.utils.ExcelUtils;
import org.apache.commons.lang3.time.DateUtils;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;

import javax.servlet.http.HttpServletResponse;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

@Controller
public class ExportExcel {
    @RequestMapping("export")
    public void export(HttpServletResponse response) {
        List<Person> personList = new ArrayList<>();
        Person person1 = new Person("路飞","1",new Date());
        Person person2 = new Person("娜美","2", DateUtils.addDays(new Date(),3));
        Person person3 = new Person("索隆","1", DateUtils.addDays(new Date(),10));
        Person person4 = new Person("小狸猫","1", DateUtils.addDays(new Date(),-10));
        personList.add(person1);
        personList.add(person2);
        personList.add(person3);
        personList.add(person4);

        ExcelUtils.exportExcel(personList, null, "导出sheet1", Person.class, "测试Person.xls", response);
    }
}
