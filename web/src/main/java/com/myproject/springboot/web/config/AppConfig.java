package com.myproject.springboot.web.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import com.myproject.springboot.web.service.MyService;

/*
Main application configuration to define spring beans
 */
@Configuration
public class AppConfig {

    @Bean
    public MyService myService() {
        return new MyService();
    }

}
