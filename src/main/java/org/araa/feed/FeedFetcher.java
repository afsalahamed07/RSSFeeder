package org.araa.feed;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.jdom2.Document;
import org.jdom2.JDOMException;
import org.jdom2.input.SAXBuilder;

import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public enum FeedFetcher {
    INSTANCE;

    private final ExecutorService executorService = Executors.newCachedThreadPool();
    private final Logger logger = LogManager.getLogger(FeedFetcher.class);

    private CompletableFuture<InputStream> openFeedStreamAsync(String feedUrl) {
        /*
         * This method opens a connection to the feed URL and returns the InputStream
         * for the feed content.
         */
        return CompletableFuture.supplyAsync(() -> {
            try {
                return openFeedStream(feedUrl);
            } catch (IOException e) {
                logger.error("Error opening feed stream", e);
                throw new RuntimeException("Error opening feed stream", e);
            }
        }, executorService);
    }


    public CompletableFuture<Document> parseFeedAsync(String feedUrl) {
        /*
         * This method asynchronously fetches and parses the feed content from the given URL.
         * It returns a CompletableFuture<Document> that will be completed with the parsed Document.
         */
        return openFeedStreamAsync(feedUrl).thenApplyAsync(feedStream -> {
            logger.info("Parsing feed from {}", feedUrl);
            try (InputStream stream = feedStream) { // Ensures that the InputStream is closed automatically
                return new SAXBuilder().build(stream);
            } catch (IOException | JDOMException e) {
                logger.error("Error parsing feed", e);
                throw new RuntimeException("Error parsing feed", e);
            }
        }, executorService)
                .exceptionally(e -> {
                    logger.error("Error parsing feed", e);
                    throw new RuntimeException("Error parsing feed", e);
                });

    }

    private InputStream openFeedStream(String feedUrl) throws IOException {
        logger.info("Opening feed stream for {}", feedUrl);
        URL url = new URL(feedUrl);
        HttpURLConnection connection = (HttpURLConnection) url.openConnection();
        connection.setRequestMethod("GET");
        connection.setConnectTimeout(15000); // 15 seconds connection timeout
        connection.setReadTimeout(15000); // 15 seconds read timeout
        connection.setRequestProperty("User-Agent", "Mozilla/5.0");
        return connection.getInputStream();
    }

    public Document parseFeed(String feedUrl) throws IOException, JDOMException {
        /*
         * This method fetches and parses the feed content from the given URL.
         * It returns the parsed Document.
         */
        try (InputStream feedStream = openFeedStream(feedUrl)) {
            return new SAXBuilder().build(feedStream);
        } // Ensures that the InputStream is closed automatically
    }
}