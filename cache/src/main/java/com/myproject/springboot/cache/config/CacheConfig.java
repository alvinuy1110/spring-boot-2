package com.myproject.springboot.cache.config;

import org.springframework.cache.CacheManager;
import org.springframework.cache.annotation.EnableCaching;
import org.springframework.cache.ehcache.EhCacheCacheManager;
import org.springframework.cache.ehcache.EhCacheManagerFactoryBean;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.io.ClassPathResource;

/*
 * Things to note:
 *
 * @EnableCaching turns on spring feature for caching.  Enables the annotation to be scanned
 * * By default, spring uses ConcurrentHashMap.  This is not good for production, as this will ultimately lead to
 * memory consumption and/or leak.
 *
 *
 */
@Configuration
@EnableCaching
public class CacheConfig {


/*
This is ehCache specific setup
 */
    @Bean
    public EhCacheManagerFactoryBean ehCacheCacheManager() {
        EhCacheManagerFactoryBean cmfb = new EhCacheManagerFactoryBean();
        cmfb.setConfigLocation(new ClassPathResource("ehcache.xml"));
        cmfb.setShared(true);
        return cmfb;
    }

    /*
    THIS is the main Spring Cache bean, which hooks to the actual cache implementation

     */
    @Bean
    public CacheManager cacheManager() {
        return new EhCacheCacheManager(ehCacheCacheManager().getObject());
    }
}
