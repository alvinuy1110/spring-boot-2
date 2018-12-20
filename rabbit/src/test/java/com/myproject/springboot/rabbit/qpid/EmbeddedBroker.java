package com.myproject.springboot.rabbit.qpid;

import java.net.URL;
import java.util.HashMap;
import java.util.Map;

import org.apache.qpid.server.SystemLauncher;

import lombok.extern.slf4j.Slf4j;

/**
 * Created by user on 07/12/18.
 */
@Slf4j
public class EmbeddedBroker {

    private static final String INITIAL_CONFIGURATION = "initial-config.json";

    final SystemLauncher systemLauncher;

    public EmbeddedBroker() {
        systemLauncher = new SystemLauncher();
    }

    public void start() {
        log.info("Starting QPID broker.....");
        try {
            systemLauncher.startup(createSystemConfig());
        }
        catch (Exception e) {
            throw new RuntimeException("Failed to start embedded QPID broker!", e);
        }
    }

    public void shutdown() {
        log.info("Shutting down broker...");
        systemLauncher.shutdown();

    }

    private Map<String, Object> createSystemConfig() {
        Map<String, Object> attributes = new HashMap<>();
        URL initialConfig = EmbeddedBroker.class.getClassLoader().getResource(INITIAL_CONFIGURATION);
        attributes.put("type", "Memory");
        attributes.put("initialConfigurationLocation", initialConfig.toExternalForm());
        attributes.put("startupLoggedToSystemOut", true);

        //        attributes.put("qpid.amqp_port", 15672);
        //        attributes.put("qpid.vhost", "localhost");

        // THIS ONE WORKS
        System.setProperty("qpid.amqp_port", "5672");
        //System.setProperty("qpid.vhost", "/");

        return attributes;
    }

    //    public EmbededQpidBroker(
    //            int amqpPort,
    //            String vhost,
    //            int managementPort,
    //            String username,
    //            String password,
    //            String configJsonUrl){
    //
    //        if(configJsonUrl != null && configJsonUrl.isEmpty()) throw new IllegalArgumentException("configJsonUrl
    // must be a valid path: '" + configJsonUrl + "'");
    //
    //
    //        this.amqpPort = amqpPort;
    //
    //        System.setProperty("qpid.amqp_port", amqpPort + "");
    //        System.setProperty("qpid.http_port", managementPort + "");
    //        System.setProperty("qpid.vhost", vhost);
    //        System.setProperty("qpid.user.name", username);
    //        System.setProperty("qpid.user.password", password);
    //
    //        brokerLauncher = new SystemLauncher();
    //    }
}
