package com.myproject.springboot.rabbit.domain;

import lombok.Data;
import lombok.ToString;

@Data
@ToString
public class MessageInfo {

    private String routingKey;
    private String exchangeName;
    private String origRoutingKey;
    private String origExchangeName;
    private int retryAttempts;
    private boolean sendToRetry;

    // in milliseconds
    private long queueDelay;
    // in milliseconds
    private long queueExpiration;
}
