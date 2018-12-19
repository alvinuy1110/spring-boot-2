package com.myproject.springboot.jpa.util;

import org.springframework.http.converter.json.MappingJackson2HttpMessageConverter;

/**
 * Some web utilities
 */
public class WebUtil {

    public static MappingJackson2HttpMessageConverter mappingJacksonHttpMessageConverter() {
        MappingJackson2HttpMessageConverter converter = new MappingJackson2HttpMessageConverter();
        converter.setObjectMapper(ObjectMapperHelper.appObjectMapper());
        return converter;
    }
}
