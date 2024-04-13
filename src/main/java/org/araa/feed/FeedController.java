package org.araa.feed;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.concurrent.CompletableFuture;


@RestController
public class FeedController {
    private static final Logger logger = LogManager.getLogger(FeedController.class);

    @GetMapping("/fetch_feed")
    public CompletableFuture<ResponseEntity<Feed>> fetchFeed(@RequestParam("rss") String rss) {
        FeedFetcher feedFetcher = FeedFetcher.INSTANCE;
        // This line will execute after the CompletableFuture<Feed> is complete
        // Return the CompletableFuture directly, chaining the asynchronous operations
        return feedFetcher.parseFeedAsync(rss)
                .thenApply(document -> {
                    // Attempt to create a Feed from the Document
                    try {
                        return new Feed(document);
                    } catch (Exception e) {
                        // If creating the Feed fails, throw an exception that will be handled by exceptionally
                        throw new RuntimeException("Failed to create feed from document", e);
                    }
                })
                .thenApply(ResponseEntity::ok)  // Wrap the Feed in a ResponseEntity
                .exceptionally(e -> {
                    // Handle any exceptions that occurred during feed fetching, parsing, or processing
                    return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(null);
                });
    }

}
