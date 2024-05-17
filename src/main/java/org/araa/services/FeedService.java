package org.araa.services;

import com.rometools.rome.io.FeedException;
import lombok.AllArgsConstructor;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.araa.application.dto.Feed;
import org.araa.application.error.FeedProcessingException;
import org.araa.domain.RSS;
import org.araa.repositories.FeedRepository;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Set;
import java.util.concurrent.CompletableFuture;

@Service
@AllArgsConstructor
public class FeedService {
    private final FeedRepository feedRepository;
    private final UserService userService;
    private static final Logger logger = LogManager.getLogger( FeedService.class );

    public CompletableFuture<Feed> fetchFeed( String rss ) {
        logger.info( "Fetching feed for {}", rss );
        return CompletableFuture.supplyAsync( () -> {
                    try {
                        return feedRepository.getFeed( rss );
                    } catch ( FeedException e ) {
                        throw new FeedProcessingException( "Error fetching Feed", e );
                    }
                } )
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
                    throw new FeedProcessingException( "Error fetching Feed", e );
                } );
    }

    public CompletableFuture<List<Feed>> getAllFeedsAsync() {
        UserDetails userDetails = ( UserDetails ) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        logger.info( "Fetching all feeds for user {}", userDetails.getUsername() );

        Set<RSS> rssList = userService.getUserSubscriptions( userDetails.getUsername() );


        List<CompletableFuture<Feed>> futureFeeds = rssList.stream()
                .map( rss -> fetchFeed( rss.getUrl() ) )
                .toList();

        return CompletableFuture.allOf( futureFeeds.toArray( new CompletableFuture[ 0 ] ) )
                .thenApply( v -> futureFeeds.stream()
                        .map( CompletableFuture::join )
                        .toList() );

    }
}
