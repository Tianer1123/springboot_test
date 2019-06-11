package com.xdclass.springboot_test.jms;
/*
 * @Author: tianer1123
 * @Description: common消费者
 * @CreateTime: 14:19 2019-06-11
 */

import org.springframework.jms.annotation.JmsListener;
import org.springframework.stereotype.Component;

@Component
public class CommonConsumer {
    @JmsListener(destination = "common.queue")
    public void receiveQueue(String text) {
        System.out.println("CommonConsumer收到的报文为：" + text);
    }
}
