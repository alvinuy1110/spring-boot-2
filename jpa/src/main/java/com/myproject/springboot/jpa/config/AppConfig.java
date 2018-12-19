package com.myproject.springboot.jpa.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import com.myproject.springboot.jpa.service.StudentService;
import com.myproject.springboot.jpa.service.StudentServiceImpl;

/*
Main application configuration to define spring beans
 */
@Configuration
public class AppConfig {

    @Bean
    public StudentService studentService() {
        return new StudentServiceImpl();
    }
}
