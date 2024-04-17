package org.araa.feed;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.AllArgsConstructor;
import org.araa.utility.FetchDocument;
import org.springframework.beans.factory.annotation.Autowired;
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
    private final FetchDocument fetchDocument = FetchDocument.INSTANCE;

    @Cacheable(value = "feed", key = "#rss", unless = "#result == null")
    public Feed getFeed(String rss) {
        RedisCache cache = (RedisCache) Objects.requireNonNull(cacheManager.getCache("feed"));
        Feed feed = cache.get(rss, Feed.class);  // This will automatically deserialize the JSON back into a Feed object

        if (feed != null) {
            // Feed was found in cache, return it
            return feed;
        } else {
            // Feed not found in cache, fetch and parse it
            try {
                feed = new Feed(fetchDocument.fetchAndParseFeed(rss));
                // Put the newly fetched feed into the cache
                cache.put(rss, feed);
                return feed;
            } catch (Exception e) {
                throw new RuntimeException("Failed to fetch and create feed from document", e);
            }
        }
    }
}
