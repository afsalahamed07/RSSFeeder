package org.araa.repositories;

import com.rometools.rome.io.FeedException;
import org.araa.application.dto.Feed;

public interface FeedRepository {
    Feed getFeed( String rss ) throws FeedException;
}
