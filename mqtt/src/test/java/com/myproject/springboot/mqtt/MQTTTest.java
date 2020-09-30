package com.myproject.springboot.mqtt;

import java.util.Random;
import java.util.UUID;

import org.eclipse.paho.client.mqttv3.IMqttClient;
import org.eclipse.paho.client.mqttv3.IMqttDeliveryToken;
import org.eclipse.paho.client.mqttv3.MqttAsyncClient;
import org.eclipse.paho.client.mqttv3.MqttCallback;
import org.eclipse.paho.client.mqttv3.MqttClient;
import org.eclipse.paho.client.mqttv3.MqttConnectOptions;
import org.eclipse.paho.client.mqttv3.MqttException;
import org.eclipse.paho.client.mqttv3.MqttMessage;
import org.eclipse.paho.client.mqttv3.logging.Logger;
import org.eclipse.paho.client.mqttv3.logging.LoggerFactory;
import org.eclipse.paho.client.mqttv3.persist.MemoryPersistence;
import org.eclipse.paho.client.mqttv3.util.Debug;
import org.testng.annotations.Test;

import lombok.SneakyThrows;
import lombok.extern.slf4j.Slf4j;

@Slf4j
public class MQTTTest {

    public static final String TOPIC = "testMqttTopic";

    @Test
    @SneakyThrows
    public void testMqtt() {

        Debug debug = null;
        //        LoggerFactory.setLogger("com.myproject");
        try {
            String url = "tcp://localhost:1883";
            MqttClient publisher = getPublisher(url);

            MqttConnectOptions options = new MqttConnectOptions();
            options.setUserName("guest");
            options.setPassword("guest".toCharArray());
            options.setAutomaticReconnect(true);
            options.setCleanSession(true);
            options.setConnectionTimeout(0);
            options.setServerURIs(new String[] {url});
            publisher.setCallback(createMqttCallbackk());

            debug = publisher.getDebug();
            
            publisher.connect(options);

            // send msg
            MqttMessage msg = createMessage();
        
        /*
        0 – “at most once” semantics, also known as “fire-and-forget”. Use this option when message loss is 
        acceptable, as it does not require any kind of acknowledgment or persistence
1 – “at least once” semantics. Use this option when message loss is not acceptable and your subscribers can handle 
duplicates
2 – “exactly once” semantics. Use this option when message loss is not acceptable and your subscribers cannot handle 
duplicates
         */
            msg.setQos(0);
            msg.setRetained(true); // kept by broker until subscriber picks up

            log.info("Payload {}", msg.getPayload());
            publisher.publish(TOPIC, msg);

            // 
            publisher.subscribe(TOPIC);
            Thread.sleep(5000);
        } finally {
            if (debug!=null) {
                debug.dumpClientDebug();
            }
        }
    }

    @SneakyThrows
    private MqttClient getPublisher(String url) {

        MemoryPersistence persistence = new MemoryPersistence();
        String publisherId = "mqtt-testPublisher"; //UUID.randomUUID().toString();
//        MqttClient publisher = new MqttClient(url, publisherId);
        MqttClient publisher = new MqttClient(url, publisherId, persistence);
        return publisher;
    }

    private MqttMessage createMessage() {
        Random rnd = new Random();
        double temp = 80 + rnd.nextDouble() * 20.0;
        byte[] payload = String.format("T:%04.2f", temp).getBytes();
        return new MqttMessage(payload);
    }

    private MqttCallback createMqttCallbackk() {
        return new MqttCallback() {

            @Override
            public void connectionLost(Throwable cause) {
                System.out.println(">>> Lost connection");
                cause.printStackTrace(); //EOFException thrown here within a few seconds
            }

            @Override
            public void messageArrived(String topic, MqttMessage message) throws Exception {
                System.out.println(">>>> messageArrived");
                
                String payload = new String(message.getPayload());
                log.info("Topic: {}, Message id:{}, payload:{}", topic, message.getId(),payload);
            }

            @Override
            public void deliveryComplete(IMqttDeliveryToken token) {
                System.out.println(">>>> deliveryComplete");
            }

        };
    }

    @Test
    @SneakyThrows
    public void testSimple() {

        String topic        = "MQTT.TOPIC";
        String content      = "Message from MqttPublishSample";
        int qos             = 0;
        String broker       = "tcp://localhost:1883";
//        broker = "tcp://amqdev.ms.otpp.net:1883";
        String clientId     = "JavaSample";
        MemoryPersistence persistence = new MemoryPersistence();

        try {
            // test to generate unique client id
            clientId = MqttAsyncClient.generateClientId();
//            MqttAsyncClient sampleClient = new MqttAsyncClient(broker, clientId, persistence);
            MqttClient sampleClient = new MqttClient(broker, clientId, persistence);
            MqttConnectOptions connOpts = new MqttConnectOptions();
            connOpts.setCleanSession(true);
            connOpts.setUserName("guest");
            connOpts.setPassword("guest".toCharArray());
            connOpts.setAutomaticReconnect(true);
            connOpts.setCleanSession(true);
//            connOpts.setConnectionTimeout(0);
            connOpts.setKeepAliveInterval(15);
            connOpts.setConnectionTimeout(30);
//            options.setServerURIs(new String[] {url});

            
            connOpts.setMqttVersion(3);
            
            System.out.println("Connecting to broker: "+broker);
            sampleClient.connect(connOpts);
            System.out.println("Connected");
            System.out.println("Publishing message: "+content);
            MqttMessage message = new MqttMessage(content.getBytes());
            message.setQos(qos);
            

            sampleClient.publish(topic, message);
            System.out.println("Message published");
            sampleClient.disconnect();
            System.out.println("Disconnected");
            System.exit(0);
        } catch(MqttException me) {
            System.out.println("reason "+me.getReasonCode());
            System.out.println("msg "+me.getMessage());
            System.out.println("loc "+me.getLocalizedMessage());
            System.out.println("cause "+me.getCause());
            System.out.println("excep "+me);
            me.printStackTrace();
        }
    }
}
