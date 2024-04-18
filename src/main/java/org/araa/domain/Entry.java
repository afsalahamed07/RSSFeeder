package org.araa.domain;

import com.rometools.rome.feed.synd.SyndCategory;
import com.rometools.rome.feed.synd.SyndContent;
import com.rometools.rome.feed.synd.SyndEntry;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.util.Date;
import java.util.List;

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
    private SyndContent description;
    private String link;
    private Date publishedDate;
    private String author;
    private List<String> categories;

    public Entry(SyndEntry entry) {
        this.title = entry.getTitle();
        this.description = entry.getDescription();
        this.link = entry.getLink();
        this.publishedDate = entry.getPublishedDate();
        this.author = entry.getAuthor();
        this.categories = extractSimpleCategories(entry.getCategories());
    }

    private List<String> extractSimpleCategories(List<SyndCategory> syndCategories) {
        return syndCategories.stream().map(SyndCategory::getName).toList();
    }

    public static Entry buildEntry(SyndEntry entry) {
        return new Entry(entry);
    }



}