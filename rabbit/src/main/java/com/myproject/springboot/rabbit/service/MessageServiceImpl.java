package com.myproject.springboot.rabbit.service;

import org.springframework.amqp.core.AmqpTemplate;
import org.springframework.amqp.core.MessagePostProcessor;
import org.springframework.beans.factory.annotation.Autowired;

import com.myproject.springboot.rabbit.domain.MessageEvent;
import lombok.extern.slf4j.Slf4j;

@Slf4j
public class MessageServiceImpl implements MessageService {

    @Autowired
    private AmqpTemplate amqpTemplate;

    @Override
    public void publish(MessageEvent messageEvent) {
        log.debug("Sending msg: {}", messageEvent);
        amqpTemplate.convertAndSend(messageEvent);
    }

    @Override
    public void publish(String exchange, String routingKey, MessageEvent messageEvent,
            MessagePostProcessor messagePostProcessor) {
        log.debug("Sending msg: {}", messageEvent);
        if (messagePostProcessor == null) {
            amqpTemplate.convertAndSend(exchange, routingKey, messageEvent);
        }
        else {
            amqpTemplate.convertAndSend(exchange, routingKey, messageEvent, messagePostProcessor);
        }
    }
}
