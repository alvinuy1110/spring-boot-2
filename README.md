# Spring Boot 2

Spring boot examples with various demos.

## Features


# Features
- [Actuator](#actuator)




## <a name="actuator"/> Actuator

Reference: https://docs.spring.io/spring-boot/docs/current/reference/html/production-ready-endpoints.html

The endpoints now have 2 different flags: (1) enabled and (2) exposed.

By default, all the endpoints are enabled except for 'shutdown'.  The endpoints are exposed via JMX and HTTP.
For HTTP, only 'info' and 'health' are exposed by default.

### Context Path

The actuator endpoints are all located under '/actuator' path by default. This can be changed by the property 'management.endpoints.web.base-path=/actuator'*[]: 
This is appended to the servlet context path. If for example the servlet context path is '/web', the actual url will be '/web/actuator'

### Endpoints

THe main endpoint (e.g. '/actuator') will give you a list of actuator endpoints exposed and their url.

### Security

* TODO



## TODO

* Web
* Logging
* Security
* Properties
* JPA
    * connection pool
    * pagination
    * H2
* Cache
* Redis
* RabbitMQ
* Testing
    * Unit Test
    * Integration Test
    * E2E Test
* Sonar
* Email
* Task scheduler
* Thymeleaf

### Others

* Cloud Config
* Eureka
* Spring Boot Admin
* Reactor