package com.myproject.springboot.activemq.jms;

import javax.jms.ConnectionFactory;
import javax.jms.JMSException;
import javax.jms.Message;
import javax.jms.MessageProducer;

import org.springframework.jms.core.JmsTemplate;

/**
 * Created by user on 12/02/19.
 */
public class ActiveMQJmsTemplate extends JmsTemplate {

    public ActiveMQJmsTemplate(ConnectionFactory connectionFactory) {
        super(connectionFactory);
    }

    @Override
    protected void doSend(MessageProducer producer, Message message) throws JMSException {
        if(this.getDeliveryDelay() >= 0L) {
            producer.setDeliveryDelay(this.getDeliveryDelay());
        }


        if(this.isExplicitQosEnabled()) {
//            producer.send(message, this.getDeliveryMode(), this.getPriority(), this.getTimeToLive());
            producer.send(message, this.getDeliveryMode(), getMessagePriority(message), this.getTimeToLive());
        } else {
            producer.send(message);
        }

    }

    private int getMessagePriority(Message message) throws JMSException{
        if (message.propertyExists(MessageProperties.PRIORITY)) {
          return  message.getIntProperty(MessageProperties.PRIORITY);
        }

        return  this.getPriority();
    }
}
