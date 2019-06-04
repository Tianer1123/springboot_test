package com.xdclass.springboot_test.task;
/*
 * @Author: tianer1123
 * @Description: 定时任务业务类
 * @CreateTime: 16:04 2019-06-04
 */

import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import java.util.Date;

@Component
public class TestTask {
    @Scheduled(fixedRate = 2000) // 两秒执行一次任务
    public void test() {
//        System.out.println("当前时间为：" + new Date());
    }
}
