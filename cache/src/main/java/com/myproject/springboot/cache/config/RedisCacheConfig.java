package com.myproject.springboot.cache.config;

import java.time.Duration;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.AutoConfigureAfter;
import org.springframework.boot.autoconfigure.cache.CacheManagerCustomizer;
import org.springframework.boot.autoconfigure.cache.CacheProperties;
import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingBean;
import org.springframework.boot.autoconfigure.data.redis.RedisAutoConfiguration;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.cache.CacheManager;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.io.ResourceLoader;
import org.springframework.data.redis.cache.RedisCacheConfiguration;
import org.springframework.data.redis.cache.RedisCacheManager;
import org.springframework.data.redis.connection.RedisConnectionFactory;
import org.springframework.data.redis.serializer.JdkSerializationRedisSerializer;
import org.springframework.data.redis.serializer.RedisSerializationContext;

/*
* This is for Redis specific.  Had to customize to have different configs per cache
 * Things to note:
 *
 *
 *
 */
@Configuration
@AutoConfigureAfter(RedisAutoConfiguration.class)
@ConditionalOnMissingBean(CacheManager.class)

@EnableConfigurationProperties({CacheProperties.class, RedisCacheProperties.class})
public class RedisCacheConfig {

    /*
    contains the "spring.cache.*" properties
     */
    @Autowired
    private CacheProperties cacheProperties;
    @Autowired
    private RedisCacheProperties redisCacheProperties;

    @Bean
    public RedisCacheManager redisCacheManager(RedisConnectionFactory
            redisConnectionFactory,  ResourceLoader resourceLoader) {


        Map<String, RedisCacheConfiguration> newCacheConfigurations = new HashMap<>();

        RedisCacheConfiguration defaultConfiguration = determineConfiguration(
                resourceLoader.getClassLoader());
        RedisCacheManager.RedisCacheManagerBuilder builder = RedisCacheManager.builder(redisConnectionFactory)
                                                                              .cacheDefaults(defaultConfiguration);


        List<String> cacheNames = this.cacheProperties.getCacheNames();
        if (!cacheNames.isEmpty()) {

            for (String cacheName : cacheNames) {

                RedisCacheConfiguration config = determineConfiguration(
                        resourceLoader.getClassLoader());

                Map<String, Long> cacheExpires =redisCacheProperties.getExpires();
                if (cacheExpires!=null && cacheExpires.containsKey(cacheName)) {
                    config= config.entryTtl(Duration.ofSeconds(cacheExpires.get(cacheName)));
                }

                // put the new config
                newCacheConfigurations.put(cacheName, config);

            }

        }

        builder.withInitialCacheConfigurations(newCacheConfigurations);

        return builder.build();



    }


    private RedisCacheConfiguration determineConfiguration(
            ClassLoader classLoader) {

        CacheProperties.Redis redisProperties = this.cacheProperties.getRedis();
        RedisCacheConfiguration config = RedisCacheConfiguration
                .defaultCacheConfig();
        config = config.serializeValuesWith(RedisSerializationContext.SerializationPair
                .fromSerializer(new JdkSerializationRedisSerializer(classLoader)));

        if (redisProperties.getTimeToLive() != null) {
            config = config.entryTtl(redisProperties.getTimeToLive());
        }
        if (redisProperties.getKeyPrefix() != null) {
            config = config.prefixKeysWith(redisProperties.getKeyPrefix());
        }
        if (!redisProperties.isCacheNullValues()) {
            config = config.disableCachingNullValues();
        }
        if (!redisProperties.isUseKeyPrefix()) {
            config = config.disableKeyPrefix();
        }
        return config;
    }
}
