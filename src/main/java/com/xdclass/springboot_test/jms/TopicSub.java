package com.xdclass.springboot_test.jms;
/*
 * @Author: tianer1123
 * @Description: 消息订阅者
 * @CreateTime: 14:36 2019-06-11
 */

import org.springframework.jms.annotation.JmsListener;
import org.springframework.stereotype.Component;

@Component
public class TopicSub {
    @JmsListener(destination = "video.topic", containerFactory = "jmsListenerContainerTopic")
    public void receive1(String text) {
        System.out.println("video.topic 消费者: receive1 = " + text);
    }
    @JmsListener(destination = "video.topic", containerFactory = "jmsListenerContainerTopic")
    public void receive2(String text) {
        System.out.println("video.topic 消费者: receive2 = " + text);
    }
    @JmsListener(destination = "video.topic", containerFactory = "jmsListenerContainerTopic")
    public void receive3(String text) {
        System.out.println("video.topic 消费者: receive3 = " + text);
    }
}
