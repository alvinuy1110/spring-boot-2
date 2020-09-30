package com.myproject.springboot.mqtt;

import java.util.Random;
import java.util.UUID;

import org.eclipse.paho.client.mqttv3.IMqttClient;
import org.eclipse.paho.client.mqttv3.IMqttDeliveryToken;
import org.eclipse.paho.client.mqttv3.MqttCallback;
import org.eclipse.paho.client.mqttv3.MqttClient;
import org.eclipse.paho.client.mqttv3.MqttConnectOptions;
import org.eclipse.paho.client.mqttv3.MqttMessage;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.integration.annotation.Gateway;
import org.springframework.integration.core.MessagingTemplate;
import org.springframework.integration.test.context.MockIntegrationContext;
import org.springframework.messaging.Message;
import org.springframework.messaging.support.MessageBuilder;
import org.springframework.test.context.testng.AbstractTestNGSpringContextTests;
import org.testng.annotations.Test;

import com.myproject.springboot.mqtt.config.MQTTConfig;
import com.myproject.springboot.mqtt.config.MQTTOutboundConfig;
import lombok.SneakyThrows;
import lombok.extern.slf4j.Slf4j;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.NONE,
        classes = {MQTTConfig.class})
@Slf4j
public class MQTTMessagingTest extends AbstractTestNGSpringContextTests {

    public static final String TOPIC = "testMqttTopic";

//@Autowired
//    private MessagingTemplate messagingTemplate;
    @Autowired
    private MQTTOutboundConfig.MyGateway gateway;
    @Test
    @SneakyThrows
    public void testMqttIntegration() {
        log.info("testMqttIntegration");

        Message message = MessageBuilder.withPayload("hello there").build();

//        MessagingTemplate messagingTemplate = new MessagingTemplate();
//        messagingTemplate.send("mqttOutboundChannel",message);
        gateway.sendToMqtt("hello there");
        
        
        // wait for message to be received
        Thread.sleep(3000);
        
    }
}