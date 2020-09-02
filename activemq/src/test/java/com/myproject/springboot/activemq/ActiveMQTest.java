package com.myproject.springboot.activemq;

import java.util.Date;
import java.util.concurrent.TimeUnit;

import org.apache.activemq.ScheduledMessage;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.EnableAutoConfiguration;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.jms.core.JmsTemplate;
import org.springframework.test.context.TestPropertySource;
import org.springframework.test.context.testng.AbstractTestNGSpringContextTests;
import org.testng.annotations.Test;

import com.myproject.springboot.activemq.config.ActiveMQConfig;
import com.myproject.springboot.activemq.consumer.Receiver;
import com.myproject.springboot.activemq.domain.Order;
import lombok.extern.slf4j.Slf4j;

import static com.myproject.springboot.activemq.config.ActiveMQConfig.ORDER_QUEUE;
import static org.assertj.core.api.Assertions.assertThat;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.NONE,
        classes = {ActiveMQTest.TestConfig.class, ActiveMQConfig.class})
@TestPropertySource(properties = "classpath:application.properties")
@Test
@Slf4j
public class ActiveMQTest extends AbstractTestNGSpringContextTests {

    @Autowired
    private JmsTemplate jmsTemplate;
    @Autowired
    private Receiver receiver;

    public void message_send_messageIsConsumed() throws Exception {

        Order myMessage = new Order(1 + " - Sending JMS Message using Embedded activeMQ", new Date());
        log.info("Sending msg");
        jmsTemplate.convertAndSend(ORDER_QUEUE, myMessage);
        receiver.getLatch().await(2000, TimeUnit.MILLISECONDS);
        assertThat(receiver.getLatch().getCount()).isEqualTo(0);

    }

    public void message_sendDelay_messageIsConsumed() throws Exception {

        Order myMessage = new Order(1 + " - Sending JMS Message using Embedded activeMQ", new Date());
        log.info("Sending msg");
        jmsTemplate.convertAndSend(ORDER_QUEUE, myMessage, m -> {
            m.setLongProperty(ScheduledMessage.AMQ_SCHEDULED_DELAY, 3000);
            return m;
        });

        receiver.getLatch().await(5000, TimeUnit.MILLISECONDS);
        assertThat(receiver.getLatch().getCount()).isEqualTo(0);

    }



//    public void message_sendPriority_messageIsConsumed() throws Exception {
//
//        Order myMessage = new Order(1 + " - Sending JMS Message using Embedded activeMQ", new Date());
//        Order myMessage2 = new Order(2 + " - Sending JMS Message using Embedded activeMQ", new Date());
//        jmsTemplate.setPriority(7); // this gets priority, message level is being ignored
//        log.info("Sending msg");
//        jmsTemplate.convertAndSend(ORDER_QUEUE, myMessage, m -> {
//            m.setJMSPriority(5);
//            m.setLongProperty(ScheduledMessage.AMQ_SCHEDULED_DELAY, 3000);
//            return m;
//        });
//        jmsTemplate.convertAndSend(ORDER_QUEUE, myMessage2, m -> {
//            m.setJMSPriority(2);
//            m.setLongProperty(ScheduledMessage.AMQ_SCHEDULED_DELAY, 3000);
//            return m;
//        });
//
//        receiver.getLatch().await(2000, TimeUnit.MILLISECONDS);
//       // assertThat(receiver.getLatch().getCount()).isEqualTo(0);
//        Thread.sleep(10000);
//
//    }

    @Configuration
    @EnableAutoConfiguration
    public static class TestConfig {

        //        @Bean
        //        public OrderConsumer orderConsumer() {
        //            return new OrderConsumer();
        //        }

        @Bean
        public Receiver receiver() {
            return new Receiver();
        }
    }
}
