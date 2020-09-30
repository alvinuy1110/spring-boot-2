MQTT
----

Sample using Spring Integration and MQTT with Artemis as broker.


## Apache ActiveMQ Artemis

### Maven dependencies
```

      <!-- https://mvnrepository.com/artifact/org.apache.activemq/activemq-amqp -->
      <dependency>
        <groupId>org.apache.activemq</groupId>
        <artifactId>activemq-mqtt</artifactId>
        <!--<version>5.16.0</version>-->
      </dependency>
      <!-- https://mvnrepository.com/artifact/org.apache.activemq/artemis-mqtt-protocol -->
      <dependency>
        <groupId>org.apache.activemq</groupId>
        <artifactId>artemis-mqtt-protocol</artifactId>
        <!-- ensure same version with broker-->
        <version>2.12.0</version>
      </dependency>
```

### Broker.xml

```
        <acceptors>
            <acceptor name="tcp">tcp://localhost:61616</acceptor>
          <acceptor name="in-vm">vm://0</acceptor>
          <acceptor name="mqtt-acceptor">tcp://localhost:1883?protocols=MQTT</acceptor>
        </acceptors>

```


## Mosquitto

MQTT Broker.

```
docker run -it --name mosquitto -p 1883:1883 eclipse-mosquitto
```

## Java MQTT Error Codes

https://www.ibm.com/support/knowledgecenter/SSFKSJ_7.5.0/com.ibm.mm.tc.doc/tc80600_.htm


## Javascript Browser

For the Browser side, MQTT is transmitted over Websockets.

This example uses Eclpise Paho Client.

### Eclipse Paho JS Doc
* https://www.eclipse.org/paho/files/jsdoc/Paho.MQTT.Client.html

### MQTT JS

Another library can be used

* https://github.com/mqttjs/MQTT.js


## References

https://github.com/gulteking/spring-boot-mqtt-sample
https://www.roytuts.com/publish-subscribe-message-onto-mqtt-using-spring/


