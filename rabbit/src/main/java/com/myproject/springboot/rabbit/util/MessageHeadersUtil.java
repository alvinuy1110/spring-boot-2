package com.myproject.springboot.rabbit.util;


import org.springframework.messaging.MessageHeaders;

import lombok.extern.slf4j.Slf4j;

@Slf4j
public class MessageHeadersUtil {


    public static void printMessageHeaders(MessageHeaders messageHeaders) {
        messageHeaders.entrySet().stream().forEach(entry -> {
            log.debug("\tKey:{}, Value:{}", entry.getKey(), entry.getValue());
        });

    }
}