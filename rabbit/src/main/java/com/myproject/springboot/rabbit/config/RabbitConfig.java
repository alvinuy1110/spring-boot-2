package com.myproject.springboot.rabbit.config;

import java.util.HashMap;
import java.util.Map;

import org.springframework.amqp.core.AcknowledgeMode;
import org.springframework.amqp.core.AmqpAdmin;
import org.springframework.amqp.core.Binding;
import org.springframework.amqp.core.BindingBuilder;
import org.springframework.amqp.core.Queue;
import org.springframework.amqp.core.TopicExchange;
import org.springframework.amqp.rabbit.annotation.EnableRabbit;
import org.springframework.amqp.rabbit.config.SimpleRabbitListenerContainerFactory;
import org.springframework.amqp.rabbit.connection.ConnectionFactory;
import org.springframework.amqp.rabbit.core.RabbitAdmin;
import org.springframework.amqp.support.converter.Jackson2JsonMessageConverter;
import org.springframework.amqp.support.converter.MessageConverter;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import com.myproject.springboot.rabbit.domain.RabbitMQHeaders;
import com.myproject.springboot.rabbit.service.BaseMessageRetryHandler;
import com.myproject.springboot.rabbit.service.MessageRetryHandler;
import com.myproject.springboot.rabbit.util.ObjectMapperHelper;

@Configuration
@EnableRabbit
public class RabbitConfig {

//    @Autowired
//    private RabbitProperties rabbitProperties;

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
        test.exchange , test.event


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
    public static final String DLX_ROUTING_KEY = "#";

    public static final String ROUTING_KEYS_DEFAULT = "test.event";
    public static final String ROUTING_KEYS_QUEUE2 = "test.event2";
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
        return BindingBuilder.bind(queue).to(exchange).with(ROUTING_KEYS_DEFAULT);
    }

    /*
        End of the first queue/ exchange definition
    */

    /*
      Start of the second queue/ exchange definition
       */
    @Bean(value = "queue2")
    Queue queue2() {
        return new Queue(queueName2, true);
    }

    @Bean
    Binding bindingQueue2(@Qualifier(value = "queue2") Queue queue, @Qualifier(value = "exchange1") TopicExchange
            exchange) {
        return BindingBuilder.bind(queue).to(exchange).with(ROUTING_KEYS_QUEUE2);
    }

    /*
        End of the second queue/ exchange definition
    */

    /*
   Start of the retry queue/ exchange definition
    */
    @Bean(value = "retryExchange")
    TopicExchange retryExchange() {
        return new TopicExchange(RETRY_NAME);
    }
    @Bean(value = "retryQueue")
    Queue retryQueue() {

        //        Map<String, Object> arguments = new HashMap<>();
        //
        //        long expiresInMilliseconds = 30*24*60*60*1000L; // 30 days
        //        arguments.put(RabbitMQHeaders.HEADER_EXPIRES, expiresInMilliseconds);
        //
        //        String queueName = RETRY_QUEUE_NAME;
        //        Queue queue = new Queue(queueName, true, false, false, arguments);

        Queue queue = new Queue(RETRY_QUEUE_NAME, true);
        return queue;
    }

    @Bean
    Binding retryBinding(@Qualifier(value = "retryQueue") Queue queue, @Qualifier(value = "retryExchange")
            TopicExchange exchange) {
        return BindingBuilder.bind(queue).to(exchange).with("retry.main");
    }
  /*
      End of the retry queue/ exchange definition
       */

    /*
      Start of the DLX queue/ exchange definition
       */
    @Bean(value = "dlx")
    TopicExchange deadLetterExchange() {
        return new TopicExchange(DLX_NAME);
    }
    @Bean(value = "dlxQueue")
    Queue deadLetterQueue() {

        Map<String, Object> arguments = new HashMap<>();

        // in milliseconds
        arguments.put(RabbitMQHeaders.HEADER_MESSAGE_TTL, 600000L);
        return new Queue(DLX_QUEUE_NAME, true, false, false, arguments);
    }

    @Bean
    Binding dlxBinding(@Qualifier(value = "dlxQueue") Queue queue, @Qualifier(value = "dlx") TopicExchange
            exchange) {
        return BindingBuilder.bind(queue).to(exchange).with(DLX_ROUTING_KEY);
    }


 /*
   End of the DLX queue/ exchange definition
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


    /*
    Custom stuff for retry
     */

    @Bean
    public MessageRetryHandler messageRetryHandler( @Qualifier(value = "retryExchange") TopicExchange exchange,
            @Qualifier(value = "dlx") TopicExchange deadLetterExchange) {
        return new BaseMessageRetryHandler(exchange, deadLetterExchange, DLX_ROUTING_KEY);
    }
}
