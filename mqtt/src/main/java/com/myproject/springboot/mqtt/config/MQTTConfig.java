package com.myproject.springboot.mqtt.config;

import org.eclipse.paho.client.mqttv3.MqttConnectOptions;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Import;
import org.springframework.integration.annotation.IntegrationComponentScan;
import org.springframework.integration.annotation.MessagingGateway;
import org.springframework.integration.annotation.ServiceActivator;
import org.springframework.integration.channel.DirectChannel;
import org.springframework.integration.config.EnableIntegration;
import org.springframework.integration.core.MessageProducer;
import org.springframework.integration.mqtt.core.DefaultMqttPahoClientFactory;
import org.springframework.integration.mqtt.core.MqttPahoClientFactory;
import org.springframework.integration.mqtt.inbound.MqttPahoMessageDrivenChannelAdapter;
import org.springframework.integration.mqtt.outbound.MqttPahoMessageHandler;
import org.springframework.integration.mqtt.support.DefaultPahoMessageConverter;
import org.springframework.messaging.Message;
import org.springframework.messaging.MessageChannel;
import org.springframework.messaging.MessageHandler;
import org.springframework.messaging.MessagingException;

import lombok.extern.slf4j.Slf4j;

@Configuration
@EnableIntegration
@IntegrationComponentScan
@Import({MQTTOutboundConfig.class})
@Slf4j
public class MQTTConfig {

    public static final String DEFAULT_MQTT_TOPIC = "testMQTTTopic";
    
    @Bean
    public MessageChannel mqttInputChannel() {
        return new DirectChannel();
    }

    @Bean
    public MqttPahoClientFactory mqttClientFactory() {
        DefaultMqttPahoClientFactory factory = new DefaultMqttPahoClientFactory();
        MqttConnectOptions options = new MqttConnectOptions();
        //        options.setServerURIs(new String[] { "tcp://host1:1883", "tcp://host2:1883" });
        //        options.setUserName("username");
        //        options.setPassword("password".toCharArray());

        
        options.setServerURIs(new String[] { "tcp://localhost:1883" });
        options.setUserName("guest");
        options.setPassword("guest".toCharArray());
        factory.setConnectionOptions(options);
        return factory;
    }
    @Bean
    public MessageProducer inbound(MqttPahoClientFactory mqttPahoClientFactory) {
       String clientId = "mqttConsumer";
        MqttPahoMessageDrivenChannelAdapter adapter =
                new MqttPahoMessageDrivenChannelAdapter(clientId, mqttPahoClientFactory, DEFAULT_MQTT_TOPIC);        
//        MqttPahoMessageDrivenChannelAdapter adapter =
//                new MqttPahoMessageDrivenChannelAdapter("tcp://localhost:1883", "testClient",
//                        "topic1", "topic2");
        


        adapter.setCompletionTimeout(5000);
        adapter.setConverter(new DefaultPahoMessageConverter());
        
        /*
        0 – “at most once” semantics, also known as “fire-and-forget”. Use this option when message loss is acceptable, as it does not require any kind of acknowledgment or persistence
1 – “at least once” semantics. Use this option when message loss is not acceptable and your subscribers can handle duplicates
2 – “exactly once” semantics. Use this option when message loss is not acceptable and your subscribers cannot handle duplicates
         */
        adapter.setQos(1);
        adapter.setOutputChannel(mqttInputChannel());
        return adapter;
    }

    @Bean
    @ServiceActivator(inputChannel = "mqttInputChannel")
    public MessageHandler handler() {
        return new MessageHandler() {

            @Override
            public void handleMessage(Message<?> message) throws MessagingException {
               
                log.info("Payload:{}", message.getPayload());
            }

        };
    }

    
}
