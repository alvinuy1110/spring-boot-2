package com.myproject.springboot.rabbit.service;

import com.myproject.springboot.rabbit.domain.MessageEvent;

public interface MessageService {

    void publish(MessageEvent messageEvent);
}
