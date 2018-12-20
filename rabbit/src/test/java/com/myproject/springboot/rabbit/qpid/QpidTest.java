package com.myproject.springboot.rabbit.qpid;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.testng.AbstractTestNGSpringContextTests;
import org.testng.annotations.Test;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.myproject.springboot.rabbit.config.AppConfig;
import com.myproject.springboot.rabbit.config.RabbitConfig;
import com.myproject.springboot.rabbit.domain.MessageEvent;
import com.myproject.springboot.rabbit.service.MessageService;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.NONE, classes={AppConfig.class, RabbitConfig
        .class, QpidConfig.class},
        properties = {"spring.profiles.active:test"})
@Test
public class QpidTest extends AbstractTestNGSpringContextTests {

    @Autowired
    private MessageService messageService;


    public void publish_and_consume() throws Exception {
        // exchange = MessagePubSubConfig.topicExchangeName;
        //  routingKey = MessagePubSubConfig.ROUTING_KEYS_DEFAULT;

        ObjectMapper objectMapper = new ObjectMapper();
        MessageEvent messageEvent = new MessageEvent();
        messageEvent.setName("test1");
        messageEvent.setDescription("desc1");
        String msg = objectMapper.writeValueAsString(messageEvent);

        System.out.println("Send msg = " + msg);

        //        MessagePostProcessor customMessagePostProcessor = (m)-> {
        //            m.getMessageProperties().setHeader(CustomMessageHeaders.HEADER_SEND_TO_RETRY,Boolean.FALSE);
        //            return m;
        //        };

        messageService.publish(messageEvent);

         Thread.sleep(3000L);
    }
}
