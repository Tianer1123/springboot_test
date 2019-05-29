package com.xdclass.springboot_test.domain;/*
 * @Author: tianer
 * @Description: easypoi导入导出测试bean
 * @CreateTime: 14:26 2019-05-20
 */

import cn.afterturn.easypoi.excel.annotation.Excel;
import lombok.*;

import java.io.Serializable;
import java.util.Date;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class Person implements Serializable {
    @Excel(name = "姓名", isImportField = "true_st")
    private String name;
    @Excel(name = "性别", replace = {"男_1", "女_2"}, orderNum = "1", isImportField = "true_st")
    private String sex;
    @Excel(name = "生日", exportFormat = "yyyy-MM-dd", importFormat = "yyyy-MM-dd", orderNum = "2", isImportField = "true_st")
    private Date birthday;
}
