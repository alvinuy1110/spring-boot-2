package com.myproject.springboot.jpa.util;

import java.io.IOException;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Sort;
import org.springframework.data.web.PageableHandlerMethodArgumentResolver;

import com.fasterxml.jackson.core.JsonGenerator;
import com.fasterxml.jackson.databind.JsonSerializer;
import com.fasterxml.jackson.databind.SerializerProvider;

/**
 * This is to customize the Page JSON response object instead of the default. Register the serializer
 */

public class PageImplJacksonSerializer extends JsonSerializer<PageImpl> {

    @Autowired
    PageableHandlerMethodArgumentResolver pageableHandlerMethodArgumentResolver;

    @Override
    public void serialize(PageImpl page, JsonGenerator jsonGenerator, SerializerProvider serializerProvider)
            throws IOException {
        jsonGenerator.writeStartObject();
        jsonGenerator.writeObjectField("content", page.getContent());
        jsonGenerator.writeBooleanField("first", page.isFirst());
        jsonGenerator.writeBooleanField("last", page.isLast());
        jsonGenerator.writeNumberField("totalPages", page.getTotalPages());
        jsonGenerator.writeNumberField("totalElements", page.getTotalElements());
        jsonGenerator.writeNumberField("numberOfElements", page.getNumberOfElements());
        jsonGenerator.writeNumberField("size", page.getSize());

        // add 1 if you want to start from 1 instead of 0
        jsonGenerator.writeNumberField("number", page.getNumber() + 1);
        Sort sort = page.getSort();

        jsonGenerator.writeArrayFieldStart("sort");
        if (sort != null) {
            for (Sort.Order order : sort) {
                jsonGenerator.writeStartObject();
                jsonGenerator.writeStringField("property", order.getProperty());
                jsonGenerator.writeStringField("direction", order.getDirection().name());
                jsonGenerator.writeBooleanField("ignoreCase", order.isIgnoreCase());
                jsonGenerator.writeStringField("nullHandling", order.getNullHandling().name());
                jsonGenerator.writeEndObject();
            }
        }
        jsonGenerator.writeEndArray();
        jsonGenerator.writeEndObject();
    }
}
