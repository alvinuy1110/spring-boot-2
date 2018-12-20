package com.myproject.springboot.rabbit.service;

import org.springframework.messaging.MessageHeaders;

public interface MessageRetryHandler {

    void sendToRetry(Object payload, MessageHeaders messageHeaders);
}
