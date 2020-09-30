package com.myproject.springboot.mqtt;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.context.ApplicationContext;
import org.springframework.integration.endpoint.IntegrationConsumer;
import org.springframework.integration.test.context.MockIntegrationContext;
import org.springframework.integration.test.context.SpringIntegrationTest;
import org.springframework.messaging.Message;
import org.springframework.messaging.MessageHandler;
import org.springframework.messaging.support.MessageBuilder;
import org.springframework.test.context.testng.AbstractTestNGSpringContextTests;
import org.testng.annotations.AfterMethod;
import org.testng.annotations.Test;

import com.myproject.springboot.mqtt.config.MQTTConfig;
import com.myproject.springboot.mqtt.config.MQTTOutboundConfig;
import lombok.SneakyThrows;
import lombok.extern.slf4j.Slf4j;

import static org.assertj.core.api.Assertions.assertThat;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.NONE,
        classes = {MQTTConfig.class})

// Here we stop the input channel
@SpringIntegrationTest(noAutoStartup = {"mqttInputChannel"})
@Slf4j
public class MQTTMessagingIntegrationTest extends AbstractTestNGSpringContextTests {

    public static final String TOPIC = "testMqttTopic";
    @Autowired
    private MockIntegrationContext mockIntegrationContext;
//@Autowired
//    private MessagingTemplate messagingTemplate;
    @Autowired
    private MQTTOutboundConfig.MyGateway gateway;


    @AfterMethod
    public void tearDown() {
        this.mockIntegrationContext.resetBeans();
    }
    
    @Autowired
    ApplicationContext applicationContext;
    @Test
    @SneakyThrows
    public void testMqttIntegration() {
        log.info("testMqttIntegration");


        MessageHandler messageHandler = (m)-> {
            
            log.info("Inside the assert handler");
            assertThat("hello there").isEqualTo(m.getPayload());
            
        };
     
        String[] beanNames= applicationContext.getBeanDefinitionNames();
        for (String beanName: beanNames) {
            System.out.println(beanName);
        }
        beanNames=applicationContext.getBeanNamesForType(IntegrationConsumer.class);
        
        // for DSL the name is the Config.bean.serviceActivator
        this.mockIntegrationContext.substituteMessageHandlerFor("MQTTConfig.handler.serviceActivator", messageHandler);

        //        MessagingTemplate messagingTemplate = new MessagingTemplate();
//        messagingTemplate.send("mqttOutboundChannel",message);
        gateway.sendToMqtt("hello there");
        
        
        // wait for message to be received
        Thread.sleep(3000);
        
    }
}