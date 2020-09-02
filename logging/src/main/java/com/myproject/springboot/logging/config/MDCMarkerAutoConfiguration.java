package com.myproject.springboot.logging.config;

import org.aspectj.lang.annotation.Aspect;
import org.springframework.boot.autoconfigure.condition.ConditionalOnClass;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.EnableAspectJAutoProxy;

import com.myproject.springboot.logging.aspect.MDCMarkerAspect;

@Configuration
@ConditionalOnClass({ MDCMarkerAspect.class, EnableAspectJAutoProxy.class, Aspect.class})
public class MDCMarkerAutoConfiguration {

    // Register as bean; or put in spring.factories!!!
    @Bean
    public MDCMarkerAspect mdcMarkerAspect() {
        return new MDCMarkerAspect();
    }
}
