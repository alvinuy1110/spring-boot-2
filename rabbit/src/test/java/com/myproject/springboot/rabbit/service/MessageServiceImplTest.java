package com.myproject.springboot.rabbit.service;

import org.springframework.amqp.core.MessagePostProcessor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.testng.AbstractTestNGSpringContextTests;
import org.testng.annotations.Test;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.myproject.springboot.rabbit.config.RabbitConfig;
import com.myproject.springboot.rabbit.domain.MessageEvent;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.NONE)
@Test
public class MessageServiceImplTest extends AbstractTestNGSpringContextTests {

    @Autowired
    private MessageService messageService;

    // Happy path
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

        // Thread.sleep(3000L);
    }

    // Happy path
        public void publish_and_consume_withException() throws Exception {
           String exchange = RabbitConfig.topicExchangeName;
           String routingKey = RabbitConfig.ROUTING_KEYS_QUEUE2;

            ObjectMapper objectMapper = new ObjectMapper();
            MessageEvent messageEvent = new MessageEvent();
            messageEvent.setName("test1");
            messageEvent.setDescription("desc1");
            String msg = objectMapper.writeValueAsString(messageEvent);

            System.out.println("Send msg = " + msg);

            MessagePostProcessor customMessagePostProcessor = (m)-> {
                m.getMessageProperties().setHeader(CustomMessageHeaders.HEADER_SEND_TO_RETRY,Boolean.TRUE);
                return m;
            };

            messageService.publish(exchange, routingKey, messageEvent, customMessagePostProcessor);
            //amqpTemplate.convertAndSend(exchange, routingKey, msg,customMessagePostProcessor);

            Thread.sleep(300000L);
        }

}
