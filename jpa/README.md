# JPA example

This will demonstrate creating a simple JPA entity/entities and expose these via REST API.

## Package structure

* domain - contains the domain
* config - spring application configuration/s
* entity - contains the entity that represents the table in java form
* repository - spring jpa repositories to interact with the datasource
* controller - web layer exposed as REST API

## Order of Learning

* Adding of Database to use.
 - [H2](#H2)
 - [Datasource Configuration](#datasource_configuration)

* Creating Entity and Repository
 - [JPA Entity](#jpa_entity)
 - [JPA Repository](#jpa_repository)

* Test the Repository

TODO: do crud operation


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
