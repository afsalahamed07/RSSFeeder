package org.araa.repositories;

import org.araa.domain.Feed;

public interface FeedRepository {
    Feed getFeed(String rss);
}
