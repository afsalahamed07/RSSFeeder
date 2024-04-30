package org.araa.services;

import lombok.AllArgsConstructor;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.araa.application.dto.Feed;
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

    public CompletableFuture<List<Feed>> getAllFeedsAsync() {
        UserDetails userDetails = ( UserDetails ) SecurityContextHolder.getContext().getAuthentication().getPrincipal();

        Set<RSS> rssList = userService.getUserSubscriptions( userDetails.getUsername() );


        List<CompletableFuture<Feed>> futureFeeds = rssList.stream()
                .map( rss -> CompletableFuture.supplyAsync(
                        () -> feedRepository.getFeed( rss.getUrl() ) )
                )
                .toList();

        return CompletableFuture.allOf( futureFeeds.toArray( new CompletableFuture[0] ) )
                .thenApply( v -> futureFeeds.stream()
                        .map( CompletableFuture::join )
                        .toList() );

    }
}
