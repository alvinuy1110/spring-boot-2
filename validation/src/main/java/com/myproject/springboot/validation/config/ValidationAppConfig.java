package com.myproject.springboot.validation.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import com.myproject.springboot.validation.service.StudentService;
import com.myproject.springboot.validation.service.impl.StudentServiceImpl;

@Configuration
public class ValidationAppConfig {

    @Bean
    public StudentService studentService() {
        return new StudentServiceImpl();
    }
}
