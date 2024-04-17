package org.araa.infrastructure.utility;

import com.rometools.rome.feed.synd.SyndFeed;
import com.rometools.rome.io.SyndFeedInput;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.jdom2.Document;
import org.jdom2.input.SAXBuilder;

import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.URL;

public class XMLParser {

    private static final Logger logger = LogManager.getLogger(XMLParser.class);

    public static InputStream openFeedStream(String feedUrl) throws IOException {
        logger.info("Opening feed stream for {}", feedUrl);
        URL url = new URL(feedUrl);
        HttpURLConnection connection = (HttpURLConnection) url.openConnection();
        connection.setRequestMethod("GET");
        connection.setConnectTimeout(15000); // 15 seconds connection timeout
        connection.setReadTimeout(15000); // 15 seconds read timeout
        connection.setRequestProperty("User-Agent", "Mozilla/5.0 (Windows NT 10.0; Win64; x64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/58.0.3029.110 Safari/537.3");
        connection.setRequestProperty("Accept", "*/*");
        connection.setRequestProperty("Connection", "keep-alive");
        return connection.getInputStream();
    }

    public static SyndFeed parse(String feedUrl) throws Exception {
        try (InputStream stream = openFeedStream(feedUrl)) {
            logger.info("Parsing feed for {}", feedUrl);
            Document document = new SAXBuilder().build(stream);
            SyndFeedInput input = new SyndFeedInput();
            return input.build(document);
        } catch (Exception e) {
            logger.error("Error parsing feed for {}", feedUrl, e);
            throw new Exception("Error parsing feed for " + feedUrl, e);
        }
    }
}