package com.myproject.springboot.rabbit.service;


import org.springframework.amqp.core.MessageProperties;
import org.springframework.messaging.MessageHeaders;

import com.myproject.springboot.rabbit.domain.MessageInfo;

public class CustomMessageHeaders {

    public static final String HEADER_ORIG_EXCHANGE="custom_origExchange";
    public static final String HEADER_ORIG_ROUTING_KEY="custom_origRoutingKey";
    public static final String HEADER_RETRY_ATTEMPT="custom_retry";
    public static final String HEADER_SEND_TO_RETRY="custom_sendToRetry";

    public static void copyMessageHeaders(MessageProperties messageProperties,final MessageInfo messageInfo) {
        messageProperties
                .setHeader(CustomMessageHeaders.HEADER_ORIG_EXCHANGE, messageInfo.getOrigExchangeName());
        messageProperties
                .setHeader(CustomMessageHeaders.HEADER_ORIG_ROUTING_KEY, messageInfo.getOrigRoutingKey());
        messageProperties.setHeader(CustomMessageHeaders.HEADER_RETRY_ATTEMPT, messageInfo.getRetryAttempts());

        messageProperties
                .setHeader(CustomMessageHeaders.HEADER_SEND_TO_RETRY, messageInfo.isSendToRetry());
    }

    public static String getOriginalExchange(final MessageHeaders messageHeaders) {
        try {
            return messageHeaders.get(CustomMessageHeaders.HEADER_ORIG_EXCHANGE, String.class);
        }
        catch (IllegalArgumentException iae) {
            return null;
        }

    }

    public static String getOriginalRoutingKey(final MessageHeaders messageHeaders) {
        try {
            return messageHeaders.get(CustomMessageHeaders.HEADER_ORIG_ROUTING_KEY, String.class);
        }
        catch (IllegalArgumentException iae) {
            return null;
        }

    }

    public static int getRetryAttempt(final MessageHeaders messageHeaders) {
        try {
            Integer integer = messageHeaders.get(CustomMessageHeaders.HEADER_RETRY_ATTEMPT, Integer.class);
            if (integer != null) {
                return integer;
            }
        }
        catch (IllegalArgumentException iae) {
            return 0;
        }

        return 0;

    }


    public static boolean isSendToRetry(final MessageHeaders messageHeaders) {

        try {
            Boolean bln = messageHeaders.get(CustomMessageHeaders.HEADER_SEND_TO_RETRY, Boolean.class);
            if (bln != null) {
                return bln;
            }
        }
        catch (IllegalArgumentException iae) {
            return false;
        }
        return false;
    }
}
