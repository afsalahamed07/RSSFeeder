package org.araa.feed;

import lombok.AllArgsConstructor;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.cache.CacheManager;
import org.springframework.stereotype.Service;

import java.util.Objects;
import java.util.concurrent.CompletableFuture;

@Service
@AllArgsConstructor
public class FeedService {
    private final CacheManager cacheManager;
    private static final Logger logger = LogManager.getLogger(FeedService.class);

    public CompletableFuture<Feed> fetchFeed(String rss) {
        FeedFetcher feedFetcher = FeedFetcher.INSTANCE;
        logger.info("Fetching feed for {}", rss);
        return feedFetcher.parseFeedAsync(rss)
                .thenApply(document -> {
                    try {
                        Feed feed = new Feed(document);
                        cacheFeed(rss, feed);
                        logger.info("Wrapping Response Entity for {}", rss);
                        return feed;
                    } catch (Exception e) {
                        throw new RuntimeException("Failed to create feed from document", e);
                    }
                })
                .exceptionally(e -> {
                    logger.error("Error fetching feed", e);
                    return null;
                });
    }

    public void cacheFeed(String rss, Feed feed) {
        if (feed != null && feed.getFeed() != null && cacheManager.getCache("feeds") != null) {
            logger.info("Caching feed for {}", rss);
            try {
                Objects.requireNonNull(cacheManager.getCache("feeds")).put(rss, feed);

            } catch (Exception e) {
                logger.error("Failed to cache feed", e);
                throw new RuntimeException("Failed to cache feed", e);

            }
        } else {
            logger.error("Failed to cache feed");
            throw new RuntimeException("Failed to cache feed");
        }
    }
}
