package com.xdclass.springboot_test.service;
/*
 * @Author: tianer1123
 * @Description: 消息生产者
 * @CreateTime: 16:37 2019-06-06
 */

import javax.jms.Destination;

public interface ProducerService {
    /**
     * 指定消息队列，还有消息
     * @param destination 指定消息队列
     * @param message 消息
     */
    void sendMessage(Destination destination, final String message);

    /**
     * 使用默认消息队列发送消息
     * @param message 消息
     */
    void sendMessage(final String message);
}
