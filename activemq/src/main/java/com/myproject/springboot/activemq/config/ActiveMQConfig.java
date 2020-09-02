package com.myproject.springboot.activemq.config;
import java.time.Duration;
import javax.jms.ConnectionFactory;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.jms.JmsProperties;
import org.springframework.boot.context.properties.PropertyMapper;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Primary;
import org.springframework.jms.annotation.EnableJms;
import org.springframework.jms.config.DefaultJmsListenerContainerFactory;
import org.springframework.jms.config.JmsListenerContainerFactory;
import org.springframework.jms.core.JmsOperations;
import org.springframework.jms.core.JmsTemplate;
import org.springframework.jms.support.converter.MappingJackson2MessageConverter;
import org.springframework.jms.support.converter.MessageConverter;
import org.springframework.jms.support.converter.MessageType;

import com.myproject.springboot.activemq.jms.ActiveMQJmsTemplate;

@EnableJms
@Configuration
public class ActiveMQConfig {

    public static final String ORDER_QUEUE = "order-queue";

    @Autowired
    private JmsProperties jmsProperties;
//    @Bean
//    public ConnectionFactory jmsConnectionFactory() {
//        final ActiveMQConnectionFactory activeMqConnectionFactory = new ActiveMQConnectionFactory(
//                this.applicationEnvironment.getJmsBrokerUrl());
//        activeMqConnectionFactory.setTrustAllPackages(true);
//        return new CachingConnectionFactory(activeMqConnectionFactory);
//    }

//    @Bean
//    public JmsOperations jmsTemplate(ConnectionFactory connectionFactory) {
//        final JmsTemplate jmsTemplate = new JmsTemplate(connectionFactory);
//        jmsTemplate.setDefaultDestinationName(PERSON_QUEUE);
//        return jmsTemplate;
//    }

   // @Primary
    @Bean
    public JmsTemplate jmsTemplate(ConnectionFactory connectionFactory, MessageConverter messageConverter) {
        final JmsTemplate jmsTemplate = new ActiveMQJmsTemplate(connectionFactory);
        //jmsTemplate.setDefaultDestinationName(PERSON_QUEUE);
        jmsTemplate.setMessageConverter(messageConverter);

        mapTemplateProperties(jmsProperties.getTemplate(), jmsTemplate);

        return jmsTemplate;
    }

    private void mapTemplateProperties(JmsProperties.Template properties, JmsTemplate template) {
        PropertyMapper map = PropertyMapper.get();
        properties.getClass();
        map.from(properties::getDefaultDestination).whenNonNull().to(template::setDefaultDestinationName);
        properties.getClass();
        map.from(properties::getDeliveryDelay).whenNonNull().as(Duration::toMillis).to(template::setDeliveryDelay);
        properties.getClass();
        map.from(properties::determineQosEnabled).to(template::setExplicitQosEnabled);
        properties.getClass();
        map.from(properties::getDeliveryMode).whenNonNull().as(JmsProperties.DeliveryMode::getValue).to(template::setDeliveryMode);
        properties.getClass();
        map.from(properties::getPriority).whenNonNull().to(template::setPriority);
        properties.getClass();
        map.from(properties::getTimeToLive).whenNonNull().as(Duration::toMillis).to(template::setTimeToLive);
        properties.getClass();
        map.from(properties::getReceiveTimeout).whenNonNull().as(Duration::toMillis).to(template::setReceiveTimeout);
    }
    @Bean
    public JmsListenerContainerFactory<?> queueListenerFactory() {
        DefaultJmsListenerContainerFactory factory = new DefaultJmsListenerContainerFactory();
        factory.setMessageConverter(messageConverter());

        factory.setSessionTransacted(true); // transactions
        //factory.setTransactionManager(); // for transactions

        factory.setMaxMessagesPerTask(1); // per thread

        return factory;
    }

    @Bean
    public MessageConverter messageConverter() {
        MappingJackson2MessageConverter converter = new MappingJackson2MessageConverter();
        converter.setTargetType(MessageType.TEXT);
        converter.setTypeIdPropertyName("_type");
        return converter;
    }

}