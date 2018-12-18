package com.myproject.springboot.web.property;

import java.util.Date;

import org.springframework.beans.factory.annotation.Value;

import lombok.Data;

/**
 * This is just a plain spring bean using @Value annotation
 */
@Data
public class MyPropertiesValue {

    @Value("${myproject.propertyRequired}")
    private String propertyRequired;

    @Value("${myproject.propertyOptional:missing value}")
    private String propertyOptional;

    @Value("#{new java.text.SimpleDateFormat('${myproject.datePropFormat}').parse('${myproject.dateProp}')}")
    private Date dateProp;
}
