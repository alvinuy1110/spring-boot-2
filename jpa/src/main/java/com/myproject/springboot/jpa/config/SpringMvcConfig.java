package com.myproject.springboot.jpa.config;

import java.util.List;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.web.PageableHandlerMethodArgumentResolver;
import org.springframework.http.converter.json.MappingJackson2HttpMessageConverter;
import org.springframework.web.method.support.HandlerMethodArgumentResolver;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

import com.myproject.springboot.jpa.util.PageImplJacksonSerializer;
import com.myproject.springboot.jpa.util.PageRequestHelper;
import com.myproject.springboot.jpa.util.WebUtil;

@Configuration
public class SpringMvcConfig implements WebMvcConfigurer {

    /**
     * This is to customize the parameter for dealing with pagination requests
     * @param argumentResolvers
     * @see {@link PageImplJacksonSerializer )
     */
    @Override
    public void addArgumentResolvers(List<HandlerMethodArgumentResolver> argumentResolvers) {
        PageableHandlerMethodArgumentResolver resolver = new PageableHandlerMethodArgumentResolver();
        resolver.setOneIndexedParameters(true); // true - will make count from page =1
        resolver.setFallbackPageable(defaultPageRequest());
        argumentResolvers.add(resolver);
    }

    @Bean
    public PageRequest defaultPageRequest() {
        // set to 0 to resemble the first page internally
        return PageRequest.of(0, 20);
    }

    @Bean
    public PageRequestHelper pageRequestHelper() {
        PageRequestHelper pageRequestHelper = new PageRequestHelper(defaultPageRequest());
        pageRequestHelper.setStartPageIndexAtOne(true);  // true - will make count from page =1
        return pageRequestHelper;
    }

    // to customize how jackson serializes object
    @Bean
    public MappingJackson2HttpMessageConverter mappingJackson2HttpMessageConverter() {
        return WebUtil.mappingJacksonHttpMessageConverter();
    }
}
