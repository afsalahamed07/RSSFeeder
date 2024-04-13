package org.araa.feed;

import com.rometools.rome.feed.synd.SyndEntry;
import com.rometools.rome.feed.synd.SyndFeed;
import com.rometools.rome.io.FeedException;
import com.rometools.rome.io.SyndFeedInput;
import lombok.AllArgsConstructor;
import lombok.Data;
import org.jdom2.Document;
import org.jdom2.JDOMException;

import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.util.List;

@Data
@AllArgsConstructor
public class Feed {
    /**
     * This is wrapper class for SyndFeed
     * It is used to extract the required fields from SyndFeed
     * and send it to the client
     */

    private SyndFeed feed;


    public Feed(Document document) throws IOException, JDOMException, FeedException {
        SyndFeedInput input = new SyndFeedInput();
        this.feed = input.build(document);
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
