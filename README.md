# Spring Boot 2

Spring boot examples with various demos.

## General References

* https://docs.spring.io/spring-boot/docs/current/reference/html/
* https://docs.spring.io/spring-boot/docs/current/reference/html/common-application-properties.html

## General Notes

* Lombok is used to simplify code
* Testing uses the following:
    * testNG
    * assertJ
    * Mockito
* Logging uses the SLF4J framework



# Features
- [Actuator](#actuator)
- [Banner](#banner)
- [Cache Module](cache/README.md)
- [JPA Module](jpa/README.md)
- [Logging](#logging)
- [Properties](#properties)
- [Web Controller](#web_controller)


## Recommended Order of Learning

1. Web project module
    * Actuator
    * Banner
    * Logging
    * Properties
    * Web controller

2. JPA module



## <a name="actuator"/> Actuator

Reference: https://docs.spring.io/spring-boot/docs/current/reference/html/production-ready-endpoints.html

The endpoints now have 2 different flags: (1) enabled and (2) exposed.

By default, all the endpoints are enabled except for 'shutdown'.  The endpoints are exposed via JMX and HTTP.
For HTTP, only 'info' and 'health' are exposed by default.

### Context Path

The actuator endpoints are all located under '/actuator' path by default. This can be changed by the property 'management.endpoints.web.base-path=/actuator'*[]: 
This is appended to the servlet context path. If for example the servlet context path is '/web', the actual url will be '/web/actuator'

### Endpoints

THe discovery page endpoint (e.g. '/actuator') will give you a list of actuator endpoints exposed and their url in hypermedia form.

#### Health

* use HealthStatusHttpMapper if you want more programmatic control
* modify the HealthAggregator if need to use custom status
* when using properties,
```
# to add custom status and order from severe to ok
management.health.status.order=FATAL, DOWN, OUT_OF_SERVICE, UNKNOWN, UP

# to customize HTTP status
management.health.status.http-mapping.FATAL=503
```

* By default, "UNKNOWN" status returns HTTP 200, which may lead to undesired behavior.  To change just add,
```
# to customize HTTP status
management.health.status.http-mapping.UNKNOWN=503
```

#### Info

##### Git Information

Reference: https://docs.spring.io/spring-boot/docs/current/reference/html/howto-build.html#howto-git-info

```
#### Whether to enable git info. (default: true)
management.info.git.enabled=true
#### Mode to use to expose git information. (values: simple, full), default:simple
management.info.git.mode=simple
```

### Security

* TODO



## <a name="banner"/> Banner

Reference: https://docs.spring.io/spring-boot/docs/current/reference/html/boot-features-spring-application.html#boot-features-banner

This is to customize the banner shown at start up.  The simplest approach is replace the file 'banner.txt' under 'src/main/resources'

For fancy text, you can use https://devops.datenkollektiv.de/banner.txt/index.html or something similar.




## <a name="logging"/> Logging

Reference: https://docs.spring.io/spring-boot/docs/current/reference/html/boot-features-logging.html

The environment property "logging.config" allows to have an external configuration to be loaded



## <a name="properties"/> Properties

Reference: https://docs.spring.io/spring-boot/docs/current/reference/html/boot-features-external-config.html

### @Value annotation

See "com.myproject.springboot.web.property.MyPropertiesValue".

### Custom Properties

Custom properties can be loaded by using '@ConfigurationProperties'. See "com.myproject.springboot.web.property.MyProperties".
See "com.myproject.springboot.web.property.MyPropertiesTest" for more demo/ explanation.

### Date converter


##### using @ConfigurationProperties

Can be done but will have to write a code like this.

```
@Component
@ConfigurationPropertiesBinding
public class LocalDateConverter implements Converter<String, LocalDate> {

    @Override
    public LocalDate convert(String timestamp) {
        return LocalDate.parse(timestamp);
    }
}
```

##### using @Value

You can also check out the @Value example.


## <a name="web_controller"/> Web Controller

This is more geared towards API purposes rather than serving up web pages.

### Controller

See "com.myproject.springboot.web.controller.MyPropertiesController"

### Testing

See "com.myproject.springboot.web.controller.MyPropertiesControllerTest"

### HTTP Client
The underlying http client will be the apache httpComponents to provide more functionality.
Used for RestTemplate and TestRemplate.

```
   <dependency>
            <groupId>org.apache.httpcomponents</groupId>
            <artifactId>httpclient</artifactId>
            <version>${httpclient.version}</version>
        </dependency>
```

#### Logging

Enable the properties similar to:
```
logging.level.org.apache.http=DEBUG
```


## TODO

* Security
* RabbitMQ
* Testing
    * Unit Test
    * Integration Test
    * E2E Test
* Sonar
* Email
* Task scheduler
* Web MVC
    * Thymeleaf

### Others

* Cloud Config
* Eureka
* Spring Boot Admin
* Reactor