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

* Define the spring bean in CacheConfig, EhCacheConfig
* external file for configuration found in "ehcache.xml"

##### Config via Properties

```
# Cache
spring.cache.type=ehcache
spring.cache.ehcache.config=classpath:ehcache.xml
```

Note: still have to use @EnableCaching


### Redis

By default, uses Lettuce instead of Jedis

##### Maven

```
<dependency>
  <groupId>org.springframework.boot</groupId>
  <artifactId>spring-boot-starter-data-redis</artifactId>
</dependency>

<dependency>
  <groupId>org.apache.commons</groupId>
  <artifactId>commons-pool2</artifactId>
</dependency>
```

##### Config via JavaConfig

* Define the spring bean in CacheConfig, RedisCacheConfig

##### Config via Properties

```

### Redis
#Redis specific configurations

spring.redis.host=localhost
spring.redis.port=6379
spring.redis.password=

spring.redis.lettuce.pool.max-active=7
spring.redis.lettuce.pool.max-idle=7
spring.redis.lettuce.pool.min-idle=2
spring.redis.lettuce.pool.max-wait=-1ms
spring.redis.lettuce.shutdown-timeout=200ms

#spring.cache.redis.cache-null-values=false
#spring.cache.redis.time-to-live=600000
#spring.cache.redis.use-key-prefix=true


#####################
# Spring cache redis
#####################
#spring.cache.cache-names= # Comma-separated list of cache names to create if supported by the underlying cache manager.
#spring.cache.redis.cache-null-values=true # Allow caching null values.
#spring.cache.redis.key-prefix= # Key prefix.
#spring.cache.redis.time-to-live= # Entry expiration. By default the entries never expire.  (in milliseconds)
#spring.cache.redis.use-key-prefix=true # Whether to use the key prefix when writing to Redis.
#spring.cache.cache-names=cache1,cache2
#spring.cache.type=redis
spring.cache.type=redis
spring.cache.cache-names=cache1,cache2

## need to indicate unit or else its milliseconds
spring.cache.redis.time-to-live=60s


# use value 0 to never expire
#spring.cache.redis.time-to-live=60
# Entry expiration. By default the entries never expire.
spring.cache.redis.key-prefix=t1
#@project.artifactId@
spring.cache.redis.use-key-prefix=true
# cache level expiration. use the format 'spring.cache.redis.<cacheName>=<expiryInSeconds>'



### Custom redis
spring.cache.redis.expires.cache2=100
#logging.level.org.springframework.cache=trace
```

Note: still have to use @EnableCaching
