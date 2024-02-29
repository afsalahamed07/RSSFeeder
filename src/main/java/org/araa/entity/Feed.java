package org.araa.entity;

import com.rometools.rome.feed.synd.SyndEntry;
import com.rometools.rome.feed.synd.SyndFeed;
import lombok.AllArgsConstructor;
import lombok.Data;

import java.net.URL;
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
