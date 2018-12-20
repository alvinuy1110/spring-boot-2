package com.myproject.springboot.rabbit.service;

import java.io.IOException;

import org.springframework.amqp.core.Message;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.amqp.support.AmqpHeaders;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.messaging.MessageHeaders;
import org.springframework.messaging.handler.annotation.Header;

import com.myproject.springboot.rabbit.config.RabbitConfig;
import com.myproject.springboot.rabbit.domain.MessageEvent;
import com.myproject.springboot.rabbit.util.MessageHeadersUtil;
import com.rabbitmq.client.Channel;
import lombok.extern.slf4j.Slf4j;

/**
 * This is a sample of Message Listener.
 *
 * Things to note:
 *
 * @RabbitListener - this marks the method to trigger when message is available for consumption.
 *   - minimum info is the queue/s to connect to
 * * the arguments can be augmented with different info supported by AMQP
 */

@Slf4j
public class MessageReceiver {

    @Autowired
    private MessageRetryHandler messageRetryHandler;
    //@Payload String payload,
    @RabbitListener(queues = RabbitConfig.queueName)
    public void receiveMessage(MessageEvent message, Message rawMessage, MessageHeaders messageHeaders,
            @Header(AmqpHeaders.CHANNEL) Channel channel, @Header(AmqpHeaders.DELIVERY_TAG) Long deliveryTag,
            @Header(AmqpHeaders.CONSUMER_TAG) String consumerTag,
            @Header(AmqpHeaders.RECEIVED_EXCHANGE) String receivedExchange,
            @Header(AmqpHeaders.RECEIVED_ROUTING_KEY) String receivedRoutingKey) throws IOException {

        try {
            log.debug("===============================\n");

            log.debug("Received <" + message + ">");
            rawMessage.getMessageProperties();
            MessageHeadersUtil.printMessageHeaders(messageHeaders);

            // log.debug("Payload >> " + payload);
            log.debug("Consumer Tag >> " + consumerTag);

            log.debug("Received exchange:{}, routing_key:{}", receivedExchange, receivedRoutingKey);

            log.debug("===============================\n");

            // this is used in conjunction with autoAcknowledge=Manual
            channel.basicAck(deliveryTag, false);
        }
        catch (Exception e) {
            channel.basicNack(deliveryTag, false, true);
            throw new RuntimeException("Error in consuming message", e);
        }

        // TODO this is temporary.  Is meant to demonstrate if Exception is thrown, should try send it for retry
        // only works after all the preprocessing does not fail (i.e. Spring conversion stuff
        //        if (CustomMessageHeaders.isSendToRetry(messageHeaders)) {
        //            log.debug("throwException is true");
        //
        //            messageRetryHandler.sendToRetry(payload, messageHeaders);
        //        }

    }


    //@Payload String payload,
    @RabbitListener(queues = RabbitConfig.queueName2)
    public void receiveMessageWithRetry(MessageEvent message, Message rawMessage, MessageHeaders messageHeaders,
            @Header(AmqpHeaders.CHANNEL) Channel channel, @Header(AmqpHeaders.DELIVERY_TAG) Long deliveryTag,
            @Header(AmqpHeaders.CONSUMER_TAG) String consumerTag,
            @Header(AmqpHeaders.RECEIVED_EXCHANGE) String receivedExchange,
            @Header(AmqpHeaders.RECEIVED_ROUTING_KEY) String receivedRoutingKey) throws IOException {

        try {
            log.debug("===============================\n");

            log.debug("Received <" + message + ">");
            rawMessage.getMessageProperties();
            MessageHeadersUtil.printMessageHeaders(messageHeaders);

            // log.debug("Payload >> " + payload);
            log.debug("Consumer Tag >> " + consumerTag);

            log.debug("Received exchange:{}, routing_key:{}", receivedExchange, receivedRoutingKey);

            log.debug("===============================\n");


            // TODO this is temporary.  Is meant to demonstrate if Exception is thrown, should try send it for retry
            //         only works after all the preprocessing does not fail (i.e. Spring conversion stuff
            if (CustomMessageHeaders.isSendToRetry(messageHeaders)) {
                log.debug("throwException is true");

                messageRetryHandler.sendToRetry(message, messageHeaders);
            }

            // this is used in conjunction with autoAcknowledge=Manual
            channel.basicAck(deliveryTag, false);
        }
        catch (Exception e) {
            channel.basicNack(deliveryTag, false, true);
            throw new RuntimeException("Error in consuming message", e);
        }



    }
}
