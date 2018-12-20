package com.myproject.springboot.rabbit.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.myproject.springboot.rabbit.service.MessageReceiver;
import com.myproject.springboot.rabbit.service.MessageService;
import com.myproject.springboot.rabbit.service.MessageServiceImpl;
import com.myproject.springboot.rabbit.util.ObjectMapperHelper;

/*
Main application configuration to define spring beans
 */
@Configuration
public class AppConfig {

    @Bean
    public MessageService messageService() {
        return new MessageServiceImpl();
    }
    @Bean
    public ObjectMapper objectMapper() {
        return ObjectMapperHelper.appObjectMapper();
    }




    @Bean
    public MessageReceiver messageReceiver() {
        return new MessageReceiver();
    }
}
