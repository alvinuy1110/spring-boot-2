package com.myproject.springboot.activemq.consumer;

import java.util.concurrent.CountDownLatch;
import javax.jms.Session;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.jms.annotation.JmsListener;
import org.springframework.messaging.Message;
import org.springframework.messaging.MessageHeaders;
import org.springframework.messaging.handler.annotation.Headers;
import org.springframework.messaging.handler.annotation.Payload;

import com.myproject.springboot.activemq.domain.Order;
import lombok.extern.slf4j.Slf4j;

import static com.myproject.springboot.activemq.config.ActiveMQConfig.ORDER_QUEUE;

@Slf4j
public class Receiver {

    private static final Logger LOGGER =
            LoggerFactory.getLogger(Receiver.class);

    private CountDownLatch latch = new CountDownLatch(1);

    public CountDownLatch getLatch() {
        return latch;
    }

    @JmsListener(destination = ORDER_QUEUE)
    public void  receiveMessage(@Payload Order order,
            @Headers MessageHeaders headers, Message message, Session session) {
        LOGGER.info("received message='{}'", message);

            log.info("received <" + order + ">");

            log.info("- - - - - - - - - - - - - - - - - - - - - - - -");
            log.info("######          Message Details           #####");
            log.info("- - - - - - - - - - - - - - - - - - - - - - - -");
            log.info("headers: " + headers);
            log.info("message: " + message);
            log.info("session: " + session);
            log.info("- - - - - - - - - - - - - - - - - - - - - - - -");
        log.info("\n\n");
        latch.countDown();
    }
}