package org.araa.application.dto;

import lombok.Data;
import org.araa.domain.RSS;

import java.util.List;

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

    public static List<RSSDto> from( List<RSS> rssList ) {
        return rssList.stream().map( RSSDto::new ).toList();
    }
}
