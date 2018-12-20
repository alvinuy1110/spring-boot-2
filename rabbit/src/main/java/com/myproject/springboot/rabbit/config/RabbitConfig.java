package com.myproject.springboot.rabbit.config;

import org.springframework.amqp.core.AcknowledgeMode;
import org.springframework.amqp.core.AmqpAdmin;
import org.springframework.amqp.core.AmqpTemplate;
import org.springframework.amqp.core.Binding;
import org.springframework.amqp.core.BindingBuilder;
import org.springframework.amqp.core.Queue;
import org.springframework.amqp.core.TopicExchange;
import org.springframework.amqp.rabbit.annotation.EnableRabbit;
import org.springframework.amqp.rabbit.config.SimpleRabbitListenerContainerFactory;
import org.springframework.amqp.rabbit.connection.ConnectionFactory;
import org.springframework.amqp.rabbit.core.RabbitAdmin;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.amqp.support.converter.Jackson2JsonMessageConverter;
import org.springframework.amqp.support.converter.MessageConverter;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.boot.autoconfigure.amqp.RabbitProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.retry.backoff.ExponentialBackOffPolicy;
import org.springframework.retry.support.RetryTemplate;

import com.myproject.springboot.rabbit.util.ObjectMapperHelper;

@Configuration
@EnableRabbit
public class RabbitConfig {

    @Autowired
    private RabbitProperties rabbitProperties;

     /* There can be 1 central retry component
             * multiple consumer to handle the volumes
             * extra app needs to be maintained

       Each consumer has their own retry

            * no need for separate app
            * has its own lifecycle
            * name must be <exchange>.<retry> to avoid collision

*/
    /*
    Exchanges:
    test.exchange  - main

    test.exchange.retry - main retry exchange

    test.dead - DLX

    Queues:
    test.queue1

        Binding  -  Routing Key:
        test.exchange , biz_event


    test.exchange.retry.queue

        Binding  -  Routing Key:
        test.retry , retry.main


    test.exchange.retry.queue.<delay>

        Binding  -  Routing Key:
        test.retry , retry.delay.<delay>

    test.dead.queue0

        Binding  -  Routing Key:
        test.dead , #

     */


    public static final String topicExchangeName = "test.exchange";

    public static final String queueName = "test.queue1";
    public static final String queueName2 = "test.queue2";

    public static final String RETRY_NAME = "test.exchange.retry";
    public static final String RETRY_QUEUE_NAME = "test.exchange.retry.queue";

    public static final String DLX_NAME = "test.dead";
    public static final String DLX_QUEUE_NAME = "test.dead.queue0";

    public static final String ROUTING_KEY = "test.event";

    /*
    Start of the first queue/ exchange definition
     */
    @Bean(value = "queue1")
    Queue queue() {
        return new Queue(queueName, true);
    }

    @Bean(value = "exchange1")
    TopicExchange exchange() {
        return new TopicExchange(topicExchangeName);
    }

    @Bean
    Binding binding(@Qualifier(value = "queue1") Queue queue, @Qualifier(value = "exchange1") TopicExchange exchange) {
        return BindingBuilder.bind(queue).to(exchange).with(ROUTING_KEY);
    }

    /*
        End of the first queue/ exchange definition
    */

    // with this, the exchange, queue, bindings are create dynamically, if not present
    // Not be careful, the parameters must match
    @Bean
    AmqpAdmin amqpAdmin(ConnectionFactory connectionFactory) {
        return new RabbitAdmin(connectionFactory);
    }



    /*

    Define the listener configuration.  This is to enable the @RabbitListener to be triggered
     */

    @Bean
    public SimpleRabbitListenerContainerFactory rabbitListenerContainerFactory(ConnectionFactory connectionFactory)

    {
        SimpleRabbitListenerContainerFactory factory = new SimpleRabbitListenerContainerFactory();
        factory.setConnectionFactory(connectionFactory);
        factory.setConcurrentConsumers(10); //Integer.parseInt(this.concurrentConsumers));

        factory.setMaxConcurrentConsumers(20); //Integer.parseInt(this.maxConcurrentConsumers));
        factory.setMessageConverter(new Jackson2JsonMessageConverter());
        factory.setAcknowledgeMode(AcknowledgeMode.MANUAL);

        factory.setDefaultRequeueRejected(true);
        return factory;
    }

    /*
    Customize the message converter
     */
    @Bean
    public MessageConverter jsonMessageConverter() {
        return new Jackson2JsonMessageConverter(ObjectMapperHelper.appObjectMapper());
    }

//    @Bean
//    public AmqpTemplate rabbitTemplate(ConnectionFactory connectionFactory) {
//        final RabbitTemplate rabbitTemplate = new RabbitTemplate(connectionFactory);
//        rabbitTemplate.setMessageConverter(jsonMessageConverter());
//
//        // adding retry!!!!
//        RetryTemplate retryTemplate = new RetryTemplate();
//        ExponentialBackOffPolicy backOffPolicy = new ExponentialBackOffPolicy();
//        backOffPolicy.setInitialInterval(500);
//        backOffPolicy.setMultiplier(10.0);
//        backOffPolicy.setMaxInterval(10000);
//        retryTemplate.setBackOffPolicy(backOffPolicy);
//        rabbitTemplate.setRetryTemplate(retryTemplate);
//
//        return rabbitTemplate;
//    }
}
