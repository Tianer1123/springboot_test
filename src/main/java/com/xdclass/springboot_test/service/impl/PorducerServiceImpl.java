package com.xdclass.springboot_test.service.impl;
/*
 * @Author: tianer1123
 * @Description: 消息生产者实现类
 * @CreateTime: 16:41 2019-06-06
 */

import com.xdclass.springboot_test.service.ProducerService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jms.core.JmsMessagingTemplate;
import org.springframework.stereotype.Service;
import javax.jms.Destination;
import javax.jms.Queue;


@Service
public class PorducerServiceImpl implements ProducerService {

    // 用来发送消息到broker的对象
    @Autowired
    private JmsMessagingTemplate jmsTemplate;

    @Autowired
    private Queue queue;

    // 发送消息，destination 是发送到的队列，message是待发送的消息。
    @Override
    public void sendMessage(Destination destination, final String message) {
        jmsTemplate.convertAndSend(destination, message);
    }

    // 发送消息，destination 是发送到的队列，message是待发送的消息。
    @Override
    public void sendMessage(final String message) {
        jmsTemplate.convertAndSend(this.queue, message);
    }
}
