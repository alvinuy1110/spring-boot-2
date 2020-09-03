package com.myproject.springboot.events.config;

import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingBean;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.event.ApplicationEventMulticaster;
import org.springframework.scheduling.annotation.EnableAsync;

import com.myproject.springboot.events.service.OrderProcessor;
import com.myproject.springboot.events.service.OrderService;

@Configuration
@EnableAsync
public class EventConfig {

    @Bean
    @ConditionalOnMissingBean
    public OrderProcessor orderProcessor() {
        return new OrderProcessor();
    }

    @Bean
    @ConditionalOnMissingBean
    public OrderService orderService(ApplicationEventPublisher applicationEventPublisher,
            ApplicationEventMulticaster applicationEventMulticaster) {
        return new OrderService(applicationEventPublisher, applicationEventMulticaster);
    }
}
