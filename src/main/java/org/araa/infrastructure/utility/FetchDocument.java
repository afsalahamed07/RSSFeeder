package org.araa.infrastructure.utility;

import com.rometools.rome.io.XmlReader;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.jdom2.Document;
import org.jdom2.JDOMException;
import org.jdom2.input.SAXBuilder;

import java.io.IOException;
import java.io.InputStream;
import java.io.Reader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public enum FetchDocument {
    INSTANCE;

    private final ExecutorService executorService = Executors.newCachedThreadPool();
    private final Logger logger = LogManager.getLogger(FetchDocument.class);

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

    public Document fetchAndParseFeed(String feedUrl) throws IOException, JDOMException {
        /*
         * This method fetches and parses the feed content from the given URL.
         * It returns the parsed Document.
         */
        try (InputStream feedStream = openFeedStream(feedUrl)) {
            return new SAXBuilder().build(feedStream);
        } // Ensures that the InputStream is closed automatically
    }
}