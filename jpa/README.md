# JPA example

This will demonstrate creating a simple JPA entity/entities and expose these via REST API.

## Package structure

* config - spring application configuration/s
* controller - web layer exposed as REST API
* domain - contains the domain object used at API level
* entity - contains the entity that represents the table in java form
* repository - spring jpa repositories to interact with the datasource
* service - business layer that contains business logic

## Order of Learning

* Adding of Database to use.
 - [H2](#H2)
 - [Datasource Configuration](#datasource_configuration)

* Creating Entity and Repository
 - [JPA Entity](#jpa_entity)
 - [JPA Repository](#jpa_repository)

* Create the Service layer
 - [Service Layer](#service_layer)

* Create the Controller
 - [Controller Layer](#controller_layer)
 - [Controller Advice](#controller_advice)
 - [Controller Testing](#controller_testing)

* Pagination support
 - create the repository
    - "StudentPagingRepository"
 - create the service
    - "StudentPagingServiceImpl"
 - create the controller
    - modify the StudentController
 - customize the paging behavior
    - see SpringMvcConfig, PageImplJacksonSerializer, SortJacksonDeserializer


## Postman API

API examples in Postman format are found in "support" directory.


## <a name="datasource_configuration"/> Datasource

Spring datasource configuration now defaults to Hikari instead of tomcat.

Reference: https://github.com/brettwooldridge/HikariCP#configuration-knobs-baby

### Spring changes

* Deprecate property
```
# deprecated
#spring.datasource.initialize=true
# replaced with
spring.datasource.initialization-mode=embedded
```

* Conflict with Spring and Hibernate
```
spring.jpa.hibernate.ddl-auto=none
```

### Logging

Turn on spring logging for more info
```
logging.level.org.springframework.jdbc.datasource=DEBUG
```

## <a name="H2"/> H2

### Maven
For this demo, we will include H2

```
    <dependency>
      <groupId>com.h2database</groupId>
      <artifactId>h2</artifactId>
      <version>1.4.187</version>
    </dependency>
```

### Properties
```
#############
# H2 Web Console (H2ConsoleProperties)
spring.h2.console.enabled=true
### Path at which the console is available.
spring.h2.console.path=/h2-console
### Whether to enable trace output.
spring.h2.console.settings.trace=false
### Whether to enable remote access.
spring.h2.console.settings.web-allow-others=false
```

### URL

The url will be the servlet context path plus the h2 console path.

Example: http://localhost:8080/jpa/h2-console


## <a name="jpa_entity"/> JPA Entity

For the JPA entity example, refer to "com.myproject.springboot.jpa.entity.StudentEntity"


## <a name="jpa_repository"/> JPA Repository

For the JPA repository example, refer to "com.myproject.springboot.jpa.repository.StudentRepository"

### Testing

See the "com.myproject.springboot.jpa.repository.StudentRepositoryTest"


## <a name="service_layer"/> Service layer

* The StudentServiceImpl is the main business logic
* The StudentServiceImpl is registered as a Spring Bean in the "com.myproject.springboot.jpa.config.AppConfig"


## <a name="controller_layer"/> Controller layer

* The StudentController is the main entry point for the REST API


## <a name="controller_advice"/> Controller Advice

* The GlobalExceptionHandler is the main error handling for all Controllers


## <a name="controller_testing"/> Controller Testing

* The AbstractControllerTest contains common logic for performing unit testing of controllers
* The StudentControllerTest demonstrates how a client tests the controller with mocks


