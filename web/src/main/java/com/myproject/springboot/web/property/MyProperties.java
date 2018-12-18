package com.myproject.springboot.web.property;

import java.util.Date;
import java.util.List;
import java.util.Map;

import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;

import lombok.Data;

/**
 * This example uses @ConfigurationProperties.  The field names are used as the identifier.
 *
 * Demonstrate:
 *
 * * simple field
 * * int
 * * boolean
 * * date
 * * list
 * * map
 *
 */
@ConfigurationProperties(prefix = "myproject")
@Data
@Component
public class MyProperties {

    private String prop1;
    private String prop2;

    private String stringProp;
    private int intProp;
    private boolean booleanProp;

    private List<String> listProp;
    private Map<String, String> mapProp;

}
