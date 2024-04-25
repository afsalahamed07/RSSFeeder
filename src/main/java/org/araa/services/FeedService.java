package org.araa.services;

import lombok.AllArgsConstructor;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.araa.application.dto.Feed;
import org.araa.repositories.FeedRepository;
import org.springframework.stereotype.Service;

import java.util.concurrent.CompletableFuture;

@Service
@AllArgsConstructor
public class FeedService {
    private final FeedRepository feedRepository;
    private static final Logger logger = LogManager.getLogger( FeedService.class );

    public CompletableFuture<Feed> fetchFeed( String rss ) {
        logger.info( "Fetching feed for {}", rss );
        return CompletableFuture.supplyAsync( () -> feedRepository.getFeed( rss ) )
                .thenApply( feed -> {
                    if ( feed != null ) {
                        logger.info( "Feed fetched successfully for {}", rss );
                    } else {
                        logger.error( "No feed found for {}", rss );
                    }
                    return feed;
                } )
                .exceptionally( e -> {
                    logger.error( "Error fetching feed for {}", rss, e );
                    throw new RuntimeException( "Error fetching feed for " + rss, e );
                } );
    }

}
