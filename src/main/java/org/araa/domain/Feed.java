package org.araa.domain;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.rometools.rome.feed.synd.SyndFeed;
import com.rometools.rome.io.FeedException;
import com.rometools.rome.io.SyndFeedInput;
import com.rometools.rome.io.XmlReader;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.jdom2.Document;

import java.io.IOException;
import java.io.Reader;
import java.util.List;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class Feed {
    /**
     * This is wrapper class for SyndFeed
     * It is used to extract the required fields from SyndFeed
     * and send it to the client
     */
    private final static Logger logger = LogManager.getLogger(Feed.class);

    private String title;
    private String description;
    private String feedType;
    private String link;
    private List<Entry> entries;


    public Feed(SyndFeed syndFeed) {
        this.title = syndFeed.getTitle();
        this.description = syndFeed.getDescription();
        this.feedType = syndFeed.getFeedType();
        this.link = syndFeed.getLink();
        this.entries = syndFeed.getEntries().stream().map(Entry::buildEntry).toList();
    }

    // Serialize this Feed object to a JSON string
    public static String serializeToJson(Feed feed) throws IOException {
        ObjectMapper mapper = new ObjectMapper();
        return mapper.writeValueAsString(feed);
    }

    // Deserialize a Feed object from a JSON string
    public static Feed deserializeFromJson(String json) throws IOException {
        ObjectMapper mapper = new ObjectMapper();
        return mapper.readValue(json, Feed.class);
    }

}
