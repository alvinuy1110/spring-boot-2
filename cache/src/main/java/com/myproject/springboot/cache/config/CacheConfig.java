package com.myproject.springboot.cache.config;

import org.springframework.cache.annotation.EnableCaching;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Import;

/*
 * Things to note:
 *
 * @EnableCaching turns on spring feature for caching.  Enables the annotation to be scanned
 * * By default, spring uses ConcurrentHashMap.  This is not good for production, as this will ultimately lead to
 * memory consumption and/or leak.
 * @Import - allows adding other configurations to be pulled ind
 *
 */
@Configuration
@EnableCaching
//@Import(value={EhCacheConfig.class}) // if enable- used for ehcache only
@Import(value = {RedisCacheConfig.class}) // if enable- used for redis only
public class CacheConfig {

}
