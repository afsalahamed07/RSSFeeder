package org.araa.feed;

import org.araa.utility.FetchDocument;
import org.springframework.cache.CacheManager;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.stereotype.Repository;

@Repository
public class FeedRepository {
    private CacheManager cacheManager;
    private final FetchDocument fetchDocument = FetchDocument.INSTANCE;

    @Cacheable(value = "feeds", key = "#rss", unless = "#result == null")
    public Feed getFeed(String rss) {
        try {
            // Fetching logic here, assuming synchronous for simplicity
            return new Feed(fetchDocument.fetchAndParseFeed(rss)); // parseFeedSync needs to be created or adapted
        } catch (Exception e) {
            throw new RuntimeException("Failed to fetch and create feed from document", e);
        }
    }
}
