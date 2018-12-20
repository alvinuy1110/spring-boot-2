package com.myproject.springboot.rabbit.service;

import org.springframework.amqp.core.AmqpTemplate;
import org.springframework.beans.factory.annotation.Autowired;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
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
}
