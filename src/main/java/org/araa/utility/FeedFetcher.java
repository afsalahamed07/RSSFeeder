package org.araa.utility;

import org.jdom2.Document;
import org.jdom2.JDOMException;

import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.URL;

public enum FeedFetcher {
    INSTANCE;

    private InputStream openFeedStream(String feedUrl) throws IOException {
        URL url = new URL(feedUrl);
        HttpURLConnection connection = (HttpURLConnection) url.openConnection();
        connection.setRequestMethod("GET");
        connection.setConnectTimeout(15000); // 15 seconds connection timeout
        connection.setReadTimeout(15000); // 15 seconds read timeout
        connection.setRequestProperty("User-Agent", "Mozilla/5.0");
        return connection.getInputStream();
    }

    public Document parseFeed(String feedUrl) throws IOException, JDOMException {
        try (InputStream feedStream = openFeedStream(feedUrl)) {
            return new org.jdom2.input.SAXBuilder().build(feedStream);
        } // Ensures that the InputStream is closed automatically
    }
}