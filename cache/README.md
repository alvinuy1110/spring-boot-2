# Cache example

This will demonstrate using Spring Cache.

## Package structure

* config - spring application configuration/s
* controller - web layer exposed as REST API
* domain - contains the domain object used at API level
* service - business layer that contains business logic





## Order of Learning

* Adding of domain stuff
 - Domain
 - Service
 - Controller

* Add Caching Config
 - [Caching config](#caching_config)



## <a name="caching_config"/> Caching Config

Reference: https://docs.spring.io/spring-boot/docs/current/reference/html/boot-features-caching.html

See "CacheConfig"


### Ehcache

##### Maven

```
    <dependency>
      <groupId>net.sf.ehcache</groupId>
      <artifactId>ehcache</artifactId>
    </dependency>
```

##### Config via JavaConfig

* Define the spring bean in CacheConfig
* external file for configuration found in "ehcache.xml"

##### Config via Properties

```
# Cache
spring.cache.type=ehcache
spring.cache.ehcache.config=classpath:ehcache.xml
```

Note: still have to use @EnableCaching

