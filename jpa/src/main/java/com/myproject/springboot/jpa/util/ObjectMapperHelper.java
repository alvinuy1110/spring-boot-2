package com.myproject.springboot.jpa.util;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializationFeature;
import com.fasterxml.jackson.databind.module.SimpleModule;

/**
 * This is configure Jackson settings for the app to use
 */
public class ObjectMapperHelper {
    public static ObjectMapper appObjectMapper() {
        ObjectMapper objectMapper = new ObjectMapper();

        objectMapper.enable(SerializationFeature.INDENT_OUTPUT); // Enable pretty printing for JSON
        objectMapper.setSerializationInclusion(JsonInclude.Include.NON_NULL);
        objectMapper.configure(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES, false);
        SimpleModule appModule = new SimpleModule("APP_MODULE");

        // register the custom serializer
        //azwsModule.addSerializer(PageImpl.class, new PageImplJacksonSerializer());
      //  azwsModule.addDeserializer(Sort.class, new SortJacksonDeserializer());
        objectMapper.registerModule(appModule);
        return objectMapper;
    }

}
