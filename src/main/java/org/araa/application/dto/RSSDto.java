package org.araa.application.dto;

import lombok.Data;
import org.araa.domain.RSS;

@Data
public class RSSDto {
    private String title;
    private String description;
    private String feedType;
    private String url;

    public RSSDto( RSS rss ) {
        url = rss.getUrl();
        feedType = rss.getFeedType();
        description = rss.getDescription();
        title = rss.getTitle();
    }
}
