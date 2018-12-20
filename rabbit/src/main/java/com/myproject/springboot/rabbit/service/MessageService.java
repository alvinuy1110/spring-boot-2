package com.myproject.springboot.rabbit.service;

import org.springframework.amqp.core.MessagePostProcessor;

import com.myproject.springboot.rabbit.domain.MessageEvent;

public interface MessageService {

    void publish(MessageEvent messageEvent);

    void publish(String exchange, String routingKey, MessageEvent messageEvent,
            MessagePostProcessor messagePostProcessor);
}
