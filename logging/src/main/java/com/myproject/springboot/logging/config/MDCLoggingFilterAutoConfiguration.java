package com.myproject.springboot.logging.config;

import javax.servlet.Filter;

import org.springframework.boot.autoconfigure.condition.ConditionalOnClass;
import org.springframework.boot.web.servlet.FilterRegistrationBean;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Conditional;
import org.springframework.context.annotation.Configuration;

import ch.qos.logback.classic.helpers.MDCInsertingServletFilter;

@Configuration
@ConditionalOnClass(Filter.class)
public class MDCLoggingFilterAutoConfiguration {

    @Bean
    public FilterRegistrationBean mdcInsertingServletFilterBean() {
        FilterRegistrationBean registrationBean = new FilterRegistrationBean();
        MDCInsertingServletFilter filter = new MDCInsertingServletFilter();
        registrationBean.setFilter(filter);
        
        // TODO lower value takes precedence
        registrationBean.setOrder(2);
        return registrationBean;
    }


    @Bean
    public FilterRegistrationBean mdcInsertingSecurityFilterBean() {
        FilterRegistrationBean registrationBean = new FilterRegistrationBean();
        MDCInsertingSecurityFilter filter = new MDCInsertingSecurityFilter();
        registrationBean.setFilter(filter);

        // TODO lower value takes precedence
        // assumes after spring security filters
        registrationBean.setOrder(1);
        return registrationBean;
    }
}
