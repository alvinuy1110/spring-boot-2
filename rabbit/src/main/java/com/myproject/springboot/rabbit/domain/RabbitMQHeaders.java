package com.myproject.springboot.rabbit.domain;

/**
 * Rabbit MQ headers supported (dependent on version)
 */
public class RabbitMQHeaders {
    public static final String HEADER_DEAD_LETTER_EXCHANGE="x-dead-letter-exchange";
    public static final String HEADER_DEAD_LETTER_ROUTING_KEY="x-dead-letter-routing-key";
    public static final String HEADER_MESSAGE_TTL="x-message-ttl";
    public static final String HEADER_EXPIRES="x-expires";
}
