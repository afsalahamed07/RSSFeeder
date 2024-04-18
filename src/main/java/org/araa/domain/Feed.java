package org.araa.domain;

import com.rometools.rome.feed.synd.SyndFeed;
import com.rometools.rome.feed.synd.SyndImage;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

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
    private SyndImage icon;
    private SyndImage image;
    private List<Entry> entries;


    public Feed(SyndFeed syndFeed) {
        this.title = syndFeed.getTitle();
        this.description = syndFeed.getDescription();
        this.feedType = syndFeed.getFeedType();
        this.link = syndFeed.getLink();
        this.entries = syndFeed.getEntries().stream().map(Entry::buildEntry).toList();
        this.icon = syndFeed.getIcon();
        this.image = syndFeed.getImage();
    }
}
