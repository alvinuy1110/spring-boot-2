package com.myproject.springboot.rabbit.service;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

import org.springframework.amqp.core.AmqpAdmin;
import org.springframework.amqp.core.AmqpTemplate;
import org.springframework.amqp.core.Binding;
import org.springframework.amqp.core.BindingBuilder;
import org.springframework.amqp.core.MessagePostProcessor;
import org.springframework.amqp.core.MessageProperties;
import org.springframework.amqp.core.Queue;
import org.springframework.amqp.core.TopicExchange;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.amqp.support.AmqpHeaders;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.messaging.MessageHeaders;
import org.springframework.messaging.handler.annotation.Header;

import com.myproject.springboot.rabbit.config.RabbitConfig;
import com.myproject.springboot.rabbit.domain.MessageInfo;
import com.myproject.springboot.rabbit.domain.RabbitMQHeaders;
import com.myproject.springboot.rabbit.util.MessageHeadersUtil;
import com.rabbitmq.client.Channel;
import lombok.Getter;
import lombok.extern.slf4j.Slf4j;

@Slf4j
public class BaseMessageRetryHandler implements MessageRetryHandler {

    @Autowired
    private AmqpTemplate amqpTemplate;

    @Autowired
    private AmqpAdmin amqpAdmin;

    //** constants
    private static final String RETRY_ROUTING_KEY_MAIN = "retry.main";

    @Getter
    private final String retryExchangeName;

    private static final long[] DEFAULT_RETRY_DELAYS = {10000L, 20000L, 50000L};

    private long[] retryDelays = DEFAULT_RETRY_DELAYS;

    private static final int DEFAULT_MAX_RETRY_ATTEMPTS = 3;

    private int maxRetryAttempts = DEFAULT_MAX_RETRY_ATTEMPTS;

    private final TopicExchange retryExchange;
    private final TopicExchange deadLetterExchange;
    private final String deadLetterRoutingKey;

    public BaseMessageRetryHandler(TopicExchange retryExchange, TopicExchange deadLetterExchange, String
            deadLetterRoutingKey) {
        this.retryExchange = retryExchange;
        this.retryExchangeName = retryExchange.getName();
        this.deadLetterExchange = deadLetterExchange;
        this.deadLetterRoutingKey = deadLetterRoutingKey;
    }

    public BaseMessageRetryHandler(TopicExchange retryExchange, TopicExchange deadLetterExchange, String
            deadLetterRoutingKey, int
            maxRetryAttempts) {
        this(retryExchange, deadLetterExchange, deadLetterRoutingKey);
        this.maxRetryAttempts = maxRetryAttempts;
    }

    public BaseMessageRetryHandler(TopicExchange retryExchange,TopicExchange deadLetterExchange, String
            deadLetterRoutingKey, int
            maxRetryAttempts, long[] retryDelays) {
        this(retryExchange, deadLetterExchange,deadLetterRoutingKey, maxRetryAttempts);

        if (retryDelays == null || retryDelays.length != maxRetryAttempts) {
            throw new IllegalArgumentException("Max retry attempts must equal to retry delays");
        }
        this.retryDelays = retryDelays;
    }

    @Override
    public void sendToRetry(Object payload, MessageHeaders messageHeaders) {

        String exchange = retryExchangeName;
        String routingKey = RETRY_ROUTING_KEY_MAIN;

        Object msg = payload;

        log.debug("===============================\n");
        log.debug("Send to RETRY msg = " + msg);

        String origExchange = messageHeaders.get(AmqpHeaders.RECEIVED_EXCHANGE, String.class);
        String origRoutingKey = messageHeaders.get(AmqpHeaders.RECEIVED_ROUTING_KEY, String.class);

        MessagePostProcessor customMessagePostProcessor = (m) -> {
            MessageProperties messageProperties = m.getMessageProperties();
            messageProperties.setHeader(CustomMessageHeaders.HEADER_ORIG_EXCHANGE, origExchange);
            messageProperties.setHeader(CustomMessageHeaders.HEADER_ORIG_ROUTING_KEY, origRoutingKey);
            messageProperties.setHeader(CustomMessageHeaders.HEADER_RETRY_ATTEMPT,
                    CustomMessageHeaders.getRetryAttempt(messageHeaders));
            messageProperties.setHeader(CustomMessageHeaders.HEADER_SEND_TO_RETRY,
                    CustomMessageHeaders.isSendToRetry(messageHeaders));
            return m;
        };
        amqpTemplate.convertAndSend(exchange, routingKey, msg, customMessagePostProcessor);

        log.debug("===============================\n");
    }

    // THIS logic parses the retry queue and sorts based on retry times or goes to perma DLX

    @RabbitListener(queues = RabbitConfig.RETRY_QUEUE_NAME)
    public void receiveRetryMessage(Object message, MessageHeaders messageHeaders,   @Header(AmqpHeaders.CHANNEL)
            Channel channel, @Header(AmqpHeaders.DELIVERY_TAG) Long deliveryTag) throws IOException {

        try {
            log.debug("===============================\n");
            log.debug("Received RETRY <" + message + ">");
            MessageHeadersUtil.printMessageHeaders(messageHeaders);
            log.debug("===============================\n");
            sendToRetrySegments(message, messageHeaders);
             // this is used in conjunction with autoAcknowledge=Manual
            channel.basicAck(deliveryTag, false);
        }
        catch (Exception e) {
            channel.basicNack(deliveryTag, false, true);
            throw new RuntimeException("Error in consuming message", e);
        }

    }

    //@Override
    public void sendToRetrySegments(Object payload, MessageHeaders messageHeaders) {
        int retryAttempt = CustomMessageHeaders.getRetryAttempt(messageHeaders);

        if (retryAttempt >= maxRetryAttempts) {
            log.debug("SEND me to DLX permanent");
            amqpTemplate.convertAndSend(this.deadLetterExchange.getName(), this.deadLetterRoutingKey, payload);
        }
        else {
            MessageInfo messageInfo = getMessageInfo(messageHeaders);

            createQueue(messageInfo);

            String routingKey = messageInfo.getRoutingKey();

            MessagePostProcessor customMessagePostProcessor = (m) -> {
                MessageProperties messageProperties = m.getMessageProperties();
                CustomMessageHeaders.copyMessageHeaders(messageProperties, messageInfo);
                return m;
            };

            log.debug("===============================\n");
            log.debug("Send retry segments");
            log.debug("Exchange:{}, RoutingKey:{}", messageInfo.getExchangeName(), routingKey);
            amqpTemplate.convertAndSend(messageInfo.getExchangeName(), routingKey, payload, customMessagePostProcessor);
            log.debug("===============================\n");
        }
    }

    private MessageInfo getMessageInfo(MessageHeaders messageHeaders) {
        MessageInfo messageInfo = new MessageInfo();

        String origExchange = CustomMessageHeaders.getOriginalExchange(messageHeaders);
        String origRoutingKey = CustomMessageHeaders.getOriginalRoutingKey(messageHeaders);
        int retryAttempt = CustomMessageHeaders.getRetryAttempt(messageHeaders);

        retryAttempt++;

        messageInfo.setOrigExchangeName(origExchange);
        messageInfo.setOrigRoutingKey(origRoutingKey);
        messageInfo.setRetryAttempts(retryAttempt);
        messageInfo.setExchangeName(RabbitConfig.RETRY_NAME);
        messageInfo.setSendToRetry(CustomMessageHeaders.isSendToRetry(messageHeaders));

        long delay = retryDelays[retryAttempt - 1];
        messageInfo.setRoutingKey("retry.delay." + Long.toString(delay));

        messageInfo.setQueueDelay(delay);
        messageInfo.setQueueExpiration(delay * 2);

        log.debug("Message Info: {}", messageInfo);
        return messageInfo;
    }

    public void createQueue(final MessageInfo messageInfo) {
        Map<String, Object> arguments = new HashMap<>();

        arguments.put(RabbitMQHeaders.HEADER_DEAD_LETTER_EXCHANGE, messageInfo.getOrigExchangeName());
        arguments.put(RabbitMQHeaders.HEADER_DEAD_LETTER_ROUTING_KEY, messageInfo.getOrigRoutingKey());
        arguments.put(RabbitMQHeaders.HEADER_MESSAGE_TTL, messageInfo.getQueueDelay());
        arguments.put(RabbitMQHeaders.HEADER_EXPIRES, messageInfo.getQueueExpiration());

        String queueName = messageInfo.getExchangeName() + ".queue." + messageInfo.getQueueDelay();
        Queue queue = new Queue(queueName, true, false, false, arguments);
        log.debug("Queue to be created: {}", queue);

        TopicExchange topicExchange = retryExchange; //new TopicExchange(messageInfo.getExchangeName());

        log.debug("declaring queue: {}", queue);
        amqpAdmin.declareQueue(queue);
        Binding binding = BindingBuilder.bind(queue).to(topicExchange).with(messageInfo.getRoutingKey());
        log.debug("declaring binding: {}", binding);
        amqpAdmin.declareBinding(binding);

    }

}
