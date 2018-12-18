package com.myproject.springboot.web.property;

import java.util.Date;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.EnableAutoConfiguration;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.TestPropertySource;
import org.springframework.test.context.testng.AbstractTestNGSpringContextTests;
import org.testng.annotations.Test;

import lombok.extern.slf4j.Slf4j;

import static org.assertj.core.api.Assertions.assertThat;

/**
 * Things to note:
 *
 * * AbstractTestNGSpringContextTests - to hook TestNG and Spring together
 * * @Test - to trigger TestNG
 * * @ContextConfiguration - to tell Spring which @Configuration to load
 * * @TestPropertySource - to tell Spring which properties to use. Using "classpath" delegates to spring
 * ResourceUtils for path resolution.
 * *
 */
@Test
@ContextConfiguration(classes = {MyPropertiesTest.MyPropertiesConfig.class})
@TestPropertySource(value = "classpath:property/myproperty.properties")
@Slf4j
public class MyPropertiesTest extends AbstractTestNGSpringContextTests {

    @Autowired
    private MyProperties properties;
    @Autowired
    private MyPropertiesValue myPropertiesValue;

    public void propertyFile_myPropertiesLoaded_propsRead() {
        log.info("Testing properties");

        assertThat(properties.getStringProp()).isEqualTo("string property");
        assertThat(properties.getIntProp()).isEqualTo(23);
        assertThat(properties.isBooleanProp()).isEqualTo(true);

        // test the list
        assertThat(properties.getListProp()).containsExactly("a", "b", "c");

        // test the map
        Map<String, String> mapProp = properties.getMapProp();
        assertThat(mapProp).isNotEmpty();
        assertThat(mapProp).hasSize(2);
        assertThat(mapProp.get("key1")).isEqualTo("value1");
        assertThat(mapProp.get("key2")).isEqualTo("value2");
    }
    public void propertyFile_myPropertiesValue_propsRead() throws Exception{
        log.info("Testing properties");

        assertThat(myPropertiesValue.getPropertyRequired()).isEqualTo("required value");
        assertThat(myPropertiesValue.getPropertyOptional()).isEqualTo("missing value");

        // date
        assertThat(myPropertiesValue.getDateProp()).isEqualTo(new java.text.SimpleDateFormat("dd-MM-yyyy").parse
                ("24-10-2010"));
    }
    /**
     *  @EnableAutoConfiguration - enables spring to do its injection magic
     *  @Configuration - tags this to be a configuration
     *  @ComponentScan - scan which directory for potential spring bean injection/ creation
     */
    @EnableAutoConfiguration
    @Configuration
    @ComponentScan(basePackages = {"com.myproject.springboot.web.property"})
    public static class MyPropertiesConfig {

        @Bean
        public MyPropertiesValue myPropertiesValue() {
            return new MyPropertiesValue();
        }
    }
}
