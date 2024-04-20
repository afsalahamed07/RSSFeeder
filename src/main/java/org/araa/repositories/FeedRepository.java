package org.araa.repositories;

import org.araa.application.dto.Feed;

public interface FeedRepository {
    Feed getFeed(String rss);
}
