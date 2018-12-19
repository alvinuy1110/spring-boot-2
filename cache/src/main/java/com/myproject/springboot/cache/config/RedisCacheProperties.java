package com.myproject.springboot.cache.config;

import java.util.Map;

import org.springframework.boot.context.properties.ConfigurationProperties;

import lombok.Data;

/**
 * This is an extension.  The current spring RedisCacheConfiguration does not cover all the use cases.  This was only
 * added recently in spring boot 2.
 *
 * Things to note:
 * * @ConfigurationProperties - allow spring to convert properties to this pojo
 */
@ConfigurationProperties(
        prefix = "spring.cache.redis"
)
@Data
public class RedisCacheProperties {
    // in seconds

//    private long timeToLive;
//    private String keyPrefix;
//    private boolean useKeyPrefix;

    // cache level expiration
    private Map<String, Long> expires;
}
