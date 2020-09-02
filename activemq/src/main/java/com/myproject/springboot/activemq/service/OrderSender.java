package com.myproject.springboot.activemq.service;
import org.apache.activemq.ScheduledMessage;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jms.core.JmsTemplate;
import org.springframework.stereotype.Service;

import com.myproject.springboot.activemq.domain.Order;

import static com.myproject.springboot.activemq.config.ActiveMQConfig.ORDER_QUEUE;

@Service
public class OrderSender {

    private static Logger log = LoggerFactory.getLogger(OrderSender.class);

    @Autowired
    private JmsTemplate jmsTemplate;

    public void send(Order myMessage) {
        log.info("sending with convertAndSend() to queue <" + myMessage + ">");

        log.info("time to live : " + jmsTemplate.getTimeToLive());
        log.info("delivery mode : " + jmsTemplate.getDeliveryMode());
        jmsTemplate.convertAndSend(ORDER_QUEUE, myMessage, m -> {
            m.setLongProperty(ScheduledMessage.AMQ_SCHEDULED_DELAY, 10000);
            return m;});

    }
}