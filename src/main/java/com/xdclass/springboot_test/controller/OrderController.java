package com.xdclass.springboot_test.controller;
/*
 * @Author: tianer1123
 * @Description: 消息队列测试controller
 * @CreateTime: 10:47 2019-06-10
 */

import com.xdclass.springboot_test.service.ProducerService;
import org.apache.activemq.command.ActiveMQQueue;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.jms.Destination;


@RestController
@RequestMapping(value = "/api/v1")
public class OrderController {

    @Autowired
    private ProducerService producerService;

    @GetMapping(value = "order")
    public Object order(String msg) {
        // 生成消息队列地址
        Destination destination = new ActiveMQQueue("order.queue");
        producerService.sendMessage(destination, msg);
        return "Send OK";
    }

    @GetMapping(value = "common")
    public Object common(String msg) {
        producerService.sendMessage(msg);
        return "Send OK";
    }
}
