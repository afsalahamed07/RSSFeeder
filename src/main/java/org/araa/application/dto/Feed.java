package org.araa.application.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.araa.domain.Entry;

import java.io.Serializable;
import java.util.List;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class Feed implements Serializable {
    /**
     * This is wrapper class for SyndFeed
     * It is used to extract the required fields from SyndFeed
     * and send it to the client
     */
    private static final Logger logger = LogManager.getLogger( Feed.class );

    private String title;
    private String description;
    private String feedType;
    private String link;
    private List<Entry> entries;
}
