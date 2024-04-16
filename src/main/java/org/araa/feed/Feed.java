package org.araa.feed;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.rometools.rome.feed.synd.SyndEntry;
import com.rometools.rome.feed.synd.SyndFeed;
import com.rometools.rome.io.FeedException;
import com.rometools.rome.io.SyndFeedInput;
import lombok.AllArgsConstructor;
import lombok.Data;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.jdom2.Document;
import org.jdom2.JDOMException;

import java.io.IOException;
import java.io.Serial;
import java.io.Serializable;
import java.nio.charset.StandardCharsets;
import java.util.List;

@Data
@AllArgsConstructor
public class Feed implements Serializable {
    /**
     * This is wrapper class for SyndFeed
     * It is used to extract the required fields from SyndFeed
     * and send it to the client
     */

    private SyndFeed feed;
    private final static Logger logger = LogManager.getLogger(Feed.class);

    public Feed(Document document) throws IOException, JDOMException, FeedException {
        logger.info("Building feed from document");
        SyndFeedInput input = new SyndFeedInput();
        this.feed = input.build(document);
    }

    // Custom serialization of feed
    @Serial
    private void writeObject(java.io.ObjectOutputStream out) throws IOException {
        out.defaultWriteObject();
        ObjectMapper mapper = new ObjectMapper();
        String feedJson = mapper.writeValueAsString(this.feed);
        out.writeObject(feedJson);
    }

    // Custom deserialize the feed
    @Serial
    private void readObject(java.io.ObjectInputStream in) throws IOException, ClassNotFoundException {
        in.defaultReadObject();
        ObjectMapper mapper = new ObjectMapper();
        String feedJson = (String) in.readObject();
        this.feed = mapper.readValue(feedJson, SyndFeed.class);
    }

    public String getTitle() {
        return feed.getTitle();
    }

    public String getDescription() {
        return feed.getDescription();
    }

    public String getLink() {
        return feed.getLink();
    }

    public String getImage() {
        return feed.getImage().getUrl();
    }

    public List<SyndEntry> getEntries() {
        return feed.getEntries();
    }

}
