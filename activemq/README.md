# ActiveMQ example

This will demonstrate using Spring JMS using ActiveMQ

## Package structure

* config - spring application configuration/s
* domain - contains the domain object used at API level
* service - business layer that contains business logic




## <a name="consumer"/> Consumer

The consumer's responsibility is to consume the messages in the queue.
There are different ways to create the consumer.  Here is one example.

### Define the Consumer

* Create a ordinary java class
* Define a business method
* Annotate the method with "@JmsListener"
* Define the new object as a Spring Bean

See "OrderConsumer"

### Customize the JMS Listener config

Here we can customize directly, specifically the messageConverter part.
```
 /*

    Define the listener configuration.  This is to enable the @JmsListener to be triggered
     */

  @Bean
    public JmsListenerContainerFactory<?> queueListenerFactory() {
        DefaultJmsListenerContainerFactory factory = new DefaultJmsListenerContainerFactory();
        factory.setMessageConverter(messageConverter());

        factory.setSessionTransacted(true); // transactions
        //factory.setTransactionManager(); // for transactions

        factory.setMaxMessagesPerTask(1); // per thread

        return factory;
    }


```


## Broker

### Customize ActiveMQ

```
# see BrokerService to see what configuration values are allowed
spring.activemq.broker-url=vm://embedded?broker.persistent=true&broker.useShutdownHook=false&broker.schedulerSupport=true
```

The different configuration at the broker will dictate behavior and features available.


### Wildcards

http://activemq.apache.org/wildcards.html

### Web UI

* https://hawt.io/


### Features

#### XML configuration

* see activemq.xml
* maven pom

```
    <!-- to support spring xml config -->
    <dependency>
      <groupId>org.apache.activemq</groupId>
      <artifactId>activemq-spring</artifactId>
    </dependency>
```

#### Schedule/ Delay messaging

* Must enable broker support "broker.schedulerSupport=true"
* For the client, pass in the "ScheduledMessage.AMQ_SCHEDULED_DELAY".  Other options are available (see http://activemq.apache.org/delay-and-schedule-message-delivery.html)

```
 public void message_sendDelay_messageIsConsumed() throws Exception {

        Order myMessage = new Order(1 + " - Sending JMS Message using Embedded activeMQ", new Date());
        log.info("Sending msg");
        jmsTemplate.convertAndSend(ORDER_QUEUE, myMessage, m -> {
            m.setLongProperty(ScheduledMessage.AMQ_SCHEDULED_DELAY, 3000);
            return m;});

        Thread.sleep(10000L);

    }

```

#### Priority Message

* http://activemq.apache.org/how-can-i-support-priority-queues.html
* see https://codenotfound.com/jms-activemq-message-priority-example.html

* producer - must set priority between 1 (low) to 9 (high) with the default 4 (medium)
* broker - set the attribute "prioritizedMessages=true" for the said queues

```
          <policyEntry queue=">" prioritizedMessages="true">
```


#### DLQ

* message is sent to DLQ after time to live

#### Persistence

* maven depedency

```
   <dependency>
      <groupId>org.apache.activemq</groupId>
      <artifactId>activemq-kahadb-store</artifactId>
    </dependency>
```

* broker setting "broker.persistent=true"




### Error Handling



## <a name="retry"/> Retry



### Define the Consumer



### Define the Exchanges/ Queues




### Define the Retry Handler



### Triggering the demo

See "ActiveMQTest"



