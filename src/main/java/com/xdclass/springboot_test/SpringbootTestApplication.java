package com.xdclass.springboot_test;

import org.apache.activemq.command.ActiveMQQueue;
import org.apache.activemq.command.ActiveMQTopic;
import org.mybatis.spring.annotation.MapperScan;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.web.servlet.ServletComponentScan;
import org.springframework.context.annotation.Bean;
import org.springframework.jms.annotation.EnableJms;
import org.springframework.jms.config.DefaultJmsListenerContainerFactory;
import org.springframework.jms.config.JmsListenerContainerFactory;
import org.springframework.scheduling.annotation.EnableAsync;
import org.springframework.scheduling.annotation.EnableScheduling;

import javax.jms.ConnectionFactory;
import javax.jms.Queue;
import javax.jms.Topic;


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

    @Bean
    public Topic topic() {
        return new ActiveMQTopic("video.topic");
    }

    public static void main(String[] args) {
        SpringApplication.run(SpringbootTestApplication.class, args);
    }


    // 需要给topic定义独立的JmsListenerContainer
    @Bean
    public JmsListenerContainerFactory<?> jmsListenerContainerTopic(ConnectionFactory activeMQConnectionFactory) {
        DefaultJmsListenerContainerFactory bean = new DefaultJmsListenerContainerFactory();
        bean.setPubSubDomain(true);
        bean.setConnectionFactory(activeMQConnectionFactory);
        return bean;
    }

}
