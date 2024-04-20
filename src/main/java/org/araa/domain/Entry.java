package org.araa.domain;

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
    private String link;
    private Date publishedDate;
    private String author;
    private List<String> categories;
    private String thumbnail;
    private String description;
}