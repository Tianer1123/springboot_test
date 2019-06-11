package com.xdclass.springboot_test.jms;
/*
 * @Author: tianer1123
 * @Description: 消费者
 * @CreateTime: 14:08 2019-06-11
 */

import org.springframework.jms.annotation.JmsListener;
import org.springframework.stereotype.Component;

@Component
public class OrderConsumer {
    @JmsListener(destination = "order.queue")
    public void receiveQueue(String text) {
        System.out.println("OrderConsumer收到的报文为：" + text);
    }
}
