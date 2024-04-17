package org.araa.domain;

import com.rometools.rome.feed.synd.SyndEntry;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.util.Date;

@AllArgsConstructor
@NoArgsConstructor
@Data
public class Entry {
    /**
     * This is wrapper class for SyndEntry
     * It is used to extract the required fields from SyndEntry
     * and send it to the client
     */

    private final static Logger logger = LogManager.getLogger(Entry.class);
    private String title;
    private String description;
    private String link;
    private Date publishedDate;
    private String author;
//    private SyndContent content;

    public Entry(SyndEntry entry) {
        this.title = entry.getTitle();
        this.description = entry.getDescription().getValue();
        this.link = entry.getLink();
        this.publishedDate = entry.getPublishedDate();
        this.author = entry.getAuthor();
//        this.content = entry.getContents().get(0);
    }

    public static Entry buildEntry(SyndEntry entry) {
        return new Entry(entry);
    }



}