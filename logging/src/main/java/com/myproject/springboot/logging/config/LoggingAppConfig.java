package com.myproject.springboot.logging.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import com.myproject.springboot.logging.aspect.MDCMarkerAspect;
import com.myproject.springboot.logging.service.StudentService;
import com.myproject.springboot.logging.service.impl.StudentServiceImpl;

@Configuration
public class LoggingAppConfig {

    @Bean
    public StudentService studentService() {
        return new StudentServiceImpl();
    }

}
