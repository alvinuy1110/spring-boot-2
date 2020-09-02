package com.myproject.springboot.activemq;

import java.util.Date;
import java.util.concurrent.TimeUnit;

import javax.jms.Message;

import org.apache.activemq.ScheduledMessage;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.EnableAutoConfiguration;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Import;
import org.springframework.context.annotation.ImportResource;
import org.springframework.context.annotation.PropertySource;
import org.springframework.jms.core.JmsTemplate;
import org.springframework.test.context.TestPropertySource;
import org.springframework.test.context.testng.AbstractTestNGSpringContextTests;
import org.testng.annotations.Test;

import com.myproject.springboot.activemq.config.ActiveMQConfig;
import com.myproject.springboot.activemq.consumer.Receiver;
import com.myproject.springboot.activemq.domain.Order;
import com.myproject.springboot.activemq.jms.MessageProperties;
import lombok.extern.slf4j.Slf4j;

import static com.myproject.springboot.activemq.config.ActiveMQConfig.ORDER_QUEUE;
import static org.assertj.core.api.Assertions.assertThat;


/*
Raw message sending and receiving.  To test the features of ActiveMQ.
 */
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.NONE,
        classes = {ActiveMQXMLTest.TestConfig.class, ActiveMQConfig.class})
@Test
@Slf4j
public class ActiveMQXMLTest extends AbstractTestNGSpringContextTests {

    @Autowired
    private JmsTemplate jmsTemplate;

    public void message_send_messageIsConsumed() throws Exception {

        Order myMessage = new Order(1 + " - Sending JMS Message using Embedded activeMQ", new Date());
        log.info("Sending msg");
        jmsTemplate.convertAndSend(ORDER_QUEUE, myMessage);
        Order msg = (Order) jmsTemplate.receiveAndConvert(ORDER_QUEUE);

        log.info("{}",msg);
        assertThat(msg).isNotNull();
        assertThat(msg.getContent()).isEqualTo(myMessage.getContent());

    }

    public void message_sendDelay_messageIsConsumed() throws Exception {

        Order myMessage = new Order(1 + " - Sending JMS Message using Embedded activeMQ", new Date());
        log.info("Sending msg");
        jmsTemplate.convertAndSend(ORDER_QUEUE, myMessage, m -> {
            m.setLongProperty(ScheduledMessage.AMQ_SCHEDULED_DELAY, 3000);
            return m;
        });

        // wait for message
        Thread.sleep(5000);

        Order msg = (Order) jmsTemplate.receiveAndConvert(ORDER_QUEUE);

        log.info("{}",msg);
        assertThat(msg).isNotNull();
        assertThat(msg.getContent()).isEqualTo(myMessage.getContent());

//        long diff = msg.getTimestamp().getTime() - myMessage.getTimestamp().getTime();
//        int diffsec = (int) (diff / (1000));
//        assertThat(diffsec).isEqualTo(myMessage.getContent());
       // assertThat(msg.getContent()).isEqualTo(myMessage.getContent());

    }

    @Test(enabled = false, description = "does not support year!!!")
    public void message_sendDelayUsingCron_messageIsConsumed() throws Exception {

        Order myMessage = new Order(1 + " - Sending JMS Message using Embedded activeMQ", new Date());
        log.info("Sending msg");
        jmsTemplate.convertAndSend(ORDER_QUEUE, myMessage, m -> {
            String cronExpression = "* * * * * *";
            m.setStringProperty(ScheduledMessage.AMQ_SCHEDULED_CRON, cronExpression);
            return m;
        });

//        receiver.getLatch().await(5000, TimeUnit.MILLISECONDS);
//        assertThat(receiver.getLatch().getCount()).isEqualTo(0);

    }

     @Test
    public void sendMessageToDLQWhenItExpires() throws InterruptedException {

        final String QUEUE_A = "queueA";
        final int ttl = 3000;
        final JmsTemplate jmsTemplate = new JmsTemplate(this.jmsTemplate.getConnectionFactory());
        jmsTemplate.setExplicitQosEnabled(true);
        jmsTemplate.setTimeToLive(ttl);

        jmsTemplate.convertAndSend(QUEUE_A, "1");
        Thread.sleep(ttl + 1000);
        jmsTemplate.setReceiveTimeout(1000);

        // not in queue
        assertThat(jmsTemplate.receive(QUEUE_A)).isNull();

        // but in DLQ
        assertThat(jmsTemplate.receive("DLQ." + QUEUE_A)).isNotNull();
    }


    public void message_sendPriority_messageIsConsumed() throws Exception {

        Order myMessage = new Order(1 + " - Sending JMS Message using Embedded activeMQ", new Date());
        Order myMessage2 = new Order(2 + " - Sending JMS Message using Embedded activeMQ", new Date());
        //jmsTemplate.setPriority(7); // this gets priority, message level is being ignored
        log.info("Sending msg");
        jmsTemplate.convertAndSend(ORDER_QUEUE, myMessage, m -> {
            //  m.setJMSPriority(4);

            m.setIntProperty(MessageProperties.PRIORITY, 2);
            m.setLongProperty(ScheduledMessage.AMQ_SCHEDULED_DELAY, 3000);
            return m;
        });
        jmsTemplate.convertAndSend(ORDER_QUEUE, myMessage2, m -> {
            // m.setJMSPriority(2);
            m.setIntProperty(MessageProperties.PRIORITY, 6);
            m.setLongProperty(ScheduledMessage.AMQ_SCHEDULED_DELAY, 3000);
            return m;
        });

        Thread.sleep(5000);

        // check that msg2 arrives first
        Order msg = (Order) jmsTemplate.receiveAndConvert(ORDER_QUEUE);

        log.info("{}",msg);
        assertThat(msg).isNotNull();
        assertThat(msg.getContent()).isEqualTo(myMessage2.getContent());

        msg = (Order) jmsTemplate.receiveAndConvert(ORDER_QUEUE);

        log.info("{}",msg);
        assertThat(msg).isNotNull();
        assertThat(msg.getContent()).isEqualTo(myMessage.getContent());
    }

    @Configuration
    @EnableAutoConfiguration
    @ImportResource("classpath:activemq.xml")
    @PropertySource(value = "classpath:application-xml.properties")
    public static class TestConfig {

//          @Bean
//        public Receiver receiver() {
//            return new Receiver();
//        }
    }
}
