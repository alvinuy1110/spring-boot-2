package com.myproject.springboot.rabbit.qpid;

import org.springframework.amqp.core.AmqpTemplate;
import org.springframework.amqp.rabbit.connection.CachingConnectionFactory;
import org.springframework.amqp.rabbit.connection.ConnectionFactory;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.amqp.support.converter.MessageConverter;
import org.springframework.boot.autoconfigure.amqp.RabbitProperties;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.retry.backoff.ExponentialBackOffPolicy;
import org.springframework.retry.support.RetryTemplate;

@Configuration
@EnableConfigurationProperties({RabbitProperties.class})
public class QpidConfig {
    //***** RABBIT CONFIG **///

    @Bean(destroyMethod = "shutdown")
    public EmbeddedBroker embeddedBroker() {
        EmbeddedBroker broker = new EmbeddedBroker();
        broker.start();
        return broker;
    }

    @Bean
    public ConnectionFactory rabbitConnectionFactory(RabbitProperties rabbitMQProperties) {
        com.rabbitmq.client.ConnectionFactory rabbitCf = new com.rabbitmq.client.ConnectionFactory();
        rabbitCf.setHost(rabbitMQProperties.getHost());

        rabbitCf.setUsername(rabbitMQProperties.getUsername());
        rabbitCf.setPassword(rabbitMQProperties.getPassword());
        rabbitCf.setRequestedHeartbeat(60);
        rabbitCf.setVirtualHost(rabbitMQProperties.getVirtualHost());
        return new CachingConnectionFactory(rabbitCf);
    }

        @Bean
        public AmqpTemplate rabbitTemplate(ConnectionFactory connectionFactory, MessageConverter messageConverter) {
            final RabbitTemplate rabbitTemplate = new RabbitTemplate(connectionFactory);
            rabbitTemplate.setMessageConverter(messageConverter);

            // adding retry!!!!
            RetryTemplate retryTemplate = new RetryTemplate();
            ExponentialBackOffPolicy backOffPolicy = new ExponentialBackOffPolicy();
            backOffPolicy.setInitialInterval(500);
            backOffPolicy.setMultiplier(10.0);
            backOffPolicy.setMaxInterval(10000);
            retryTemplate.setBackOffPolicy(backOffPolicy);
            rabbitTemplate.setRetryTemplate(retryTemplate);

            return rabbitTemplate;
        }

}
