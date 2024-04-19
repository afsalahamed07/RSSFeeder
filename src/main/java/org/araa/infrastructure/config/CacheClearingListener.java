package org.araa.infrastructure.config;

import lombok.AllArgsConstructor;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.context.ApplicationListener;
import org.springframework.context.event.ContextRefreshedEvent;
import org.springframework.data.redis.cache.RedisCacheManager;
import org.springframework.stereotype.Component;

import java.util.Objects;

@Component
@AllArgsConstructor
public class CacheClearingListener implements ApplicationListener<ContextRefreshedEvent> {
    @Qualifier("RedisCacheManager")
    private RedisCacheManager cacheManager;

    @Override
    public void onApplicationEvent(ContextRefreshedEvent event) {
        // Assuming the name of your cache is "feed"
        Objects.requireNonNull(cacheManager.getCache("feed")).clear();
    }
}
