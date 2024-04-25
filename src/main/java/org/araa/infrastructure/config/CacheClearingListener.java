package org.araa.infrastructure.config;

import lombok.NonNull;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.context.ApplicationListener;
import org.springframework.context.event.ContextRefreshedEvent;
import org.springframework.data.redis.cache.RedisCacheManager;
import org.springframework.stereotype.Component;

import java.util.Objects;

@Component
public class CacheClearingListener implements ApplicationListener<ContextRefreshedEvent> {

    private final RedisCacheManager cacheManager;

    public CacheClearingListener( @Qualifier( "redisCacheManager" ) RedisCacheManager cacheManager ) {
        this.cacheManager = cacheManager;
    }

    @Override
    public void onApplicationEvent( @NonNull ContextRefreshedEvent event ) {
        // Assuming the name of your cache is "feed"
        Objects.requireNonNull( cacheManager.getCache( "feed" ) ).clear();
    }
}
