package org.araa.repositories;

import com.rometools.rome.feed.synd.SyndFeed;
import com.rometools.rome.io.XmlReader;
import lombok.AllArgsConstructor;
import org.araa.domain.Feed;
import org.araa.infrastructure.utility.XMLParser;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.data.redis.cache.RedisCache;
import org.springframework.data.redis.cache.RedisCacheManager;
import org.springframework.stereotype.Repository;

import java.util.Objects;

@Repository
@AllArgsConstructor
public class FeedRepositoryImp implements FeedRepository {
    @Qualifier("RedisCacheManager")
    private RedisCacheManager cacheManager;

    @Cacheable(value = "feed", key = "#rss", unless = "#result == null")
    public Feed getFeed(String rss) {
        // this is required to set the correct class loader for the current thread
        Thread.currentThread().setContextClassLoader(getClass().getClassLoader());

        RedisCache cache = (RedisCache) Objects.requireNonNull(cacheManager.getCache("feed"));
        Feed feed = cache.get(rss, Feed.class);  // This will automatically deserialize the JSON back into a Feed object

        if (feed != null) {
            // Feed was found in cache, return it
            return feed;
        } else {
            // Feed not found in cache, fetch and parse it
            try {
                SyndFeed syndFeed = XMLParser.parse(rss);
                feed = new Feed(syndFeed);
                return feed;
            } catch (Exception e) {
                throw new RuntimeException("Failed to parse and create feed", e);
            }
        }
    }
}