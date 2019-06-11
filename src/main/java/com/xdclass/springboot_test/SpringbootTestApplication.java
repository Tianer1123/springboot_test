package com.xdclass.springboot_test;

import org.apache.activemq.command.ActiveMQQueue;
import org.mybatis.spring.annotation.MapperScan;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.web.servlet.ServletComponentScan;
import org.springframework.context.annotation.Bean;
import org.springframework.jms.annotation.EnableJms;
import org.springframework.scheduling.annotation.EnableAsync;
import org.springframework.scheduling.annotation.EnableScheduling;

import javax.jms.Queue;


@SpringBootApplication
@ServletComponentScan
@MapperScan("com.xdclass.springboot_test.mapper")
@EnableScheduling  // 开启定时任务
@EnableAsync       // 开启异步任务
@EnableJms         // 开启Jms支持
public class SpringbootTestApplication {

    @Bean // 交给spring管理，方便后续进行注入
    public Queue queue() {
        return new ActiveMQQueue("common.queue");
    }

    public static void main(String[] args) {
        SpringApplication.run(SpringbootTestApplication.class, args);
    }

}
