package org.araa.utility.config;

import org.springframework.cache.annotation.EnableCaching;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.redis.cache.RedisCacheConfiguration;
import org.springframework.data.redis.cache.RedisCacheManager;
import org.springframework.data.redis.connection.RedisConnectionFactory;
import org.springframework.data.redis.serializer.JdkSerializationRedisSerializer;
import org.springframework.data.redis.serializer.RedisSerializationContext;

import java.time.Duration;

@Configuration
@EnableCaching
public class RedisCacheConfig {

    @Bean( name = "redisCacheManager" )
    public RedisCacheManager cacheManager( RedisConnectionFactory connectionFactory ) {
        RedisCacheConfiguration cacheConfiguration = RedisCacheConfiguration.defaultCacheConfig()
                .entryTtl( Duration.ofMinutes( 60 ) )
                .serializeValuesWith( RedisSerializationContext.SerializationPair.fromSerializer( new JdkSerializationRedisSerializer( Thread.currentThread().getContextClassLoader() ) ) );

        return RedisCacheManager.builder( connectionFactory )
                .cacheDefaults( cacheConfiguration )
                .build();
    }
}
