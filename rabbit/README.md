# Rabbit example

This will demonstrate using Spring AMQP with RabbitMQ specifics

## Package structure

* config - spring application configuration/s
* controller - web layer exposed as REST API
* domain - contains the domain object used at API level
* service - business layer that contains business logic





## Order of Learning

* [Setup Rabbit](#setup)

* [Prepare the Exchange and Queues](#exchange_queue)

* Add the Publisher
 - Add a controller for ease of demo
 - Add the service
 - Test the service "http://localhost:8080/rabbit/publish" and chech Rabbit to see the message in the queue

* [Add the Consumers](#consumer)



## <a name="setup"/> Setup

### Install
```
sudo apt-get install rabbitmq-server
```

### Management GUI

Add the management plugin

```
sudo rabbitmq-plugins enable rabbitmq_management
```

### Start/ Stop
```
sudo service rabbitmq-server restart
```

### Management URL

http://localhost:15672/

default
user: guest
password: guest



## <a name="exchange_queue"/> Prepare Exchange and Queues

The Exchange and Queues are needs to be prepared based on the requirements of the application
### Manual

Login to the Rabbit and set the exchange/s and queue/s

### Programmatically

You can configure your app to auto-create them for you.  Do this by

```
    @Bean
    AmqpAdmin amqpAdmin(ConnectionFactory connectionFactory) {
        return new RabbitAdmin(connectionFactory);
    }
```

See "RabbitConfig"


## <a name="consumer"/> Consumer

The consumer's responsibility is to consume the messages in the queue.
There are different ways to create the consumer.  Here is one example.

### Define the Consumer

* Create a ordinary java class
* Define a business method
* Annotate the method with "@RabbitListener"
* Add optional AMQP objects in the arguments
* Define the new object as a Spring Bean

See "MessageReceiver"

### Customize the Rabbit Listener config

Here we can customize directly, specifically the messageConverter part.
```
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
        return factory;
    }

```


### Error Handling

By using RabbitListener, it is possible that an error occurs even prior entering the Java method.
Typical scenario is the message cannot be converted to the desired type expected by method.  This will cause the message to be rejected, but not necessarily requeued.
It might even be lost entirely.

To have more granular control, it is advisable to set the "AcknowledgeMode" to "MANUAL".
This will then force to have an explicit acknowledgement logic like,

```

public void receiveMessage(MessageEvent message, Message rawMessage, MessageHeaders messageHeaders,
            @Header(AmqpHeaders.CHANNEL) Channel channel,
            @Header(AmqpHeaders.DELIVERY_TAG) Long deliveryTag,
            @Header(AmqpHeaders.CONSUMER_TAG) String consumerTag,
            @Header(AmqpHeaders.RECEIVED_EXCHANGE) String receivedExchange,
            @Header(AmqpHeaders.RECEIVED_ROUTING_KEY) String receivedRoutingKey) throws IOException {

            ....

        // this is used in conjunction with autoAcknowledge=Manual
        boolean noErrors=false;
        if (noErrors) {
            channel.basicAck(deliveryTag, false);
        } else {
            channel.basicNack(deliveryTag, false, true);
        }
}
```

See "MessageReceiver"



