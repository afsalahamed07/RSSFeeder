package org.araa.entity;

import com.rometools.rome.feed.synd.SyndEntry;
import lombok.AllArgsConstructor;
import java.util.Date;

@AllArgsConstructor
public class Entry {
    /**
     * This is wrapper class for SyndEntry
     * It is used to extract the required fields from SyndEntry
     * and send it to the client
     */

    private SyndEntry entry;

    public String getTitle() {
        return entry.getTitle();
    }

    public String getLink() {
        return entry.getLink();
    }

    public String getDesc() {
        return entry.getDescription().getValue();
    }

    public Date getPublishedDate() {
        return entry.getPublishedDate();
    }

    public String getThumbnail() {
        return entry.getForeignMarkup().get(0).getAttribute("url").getValue();
    }

}