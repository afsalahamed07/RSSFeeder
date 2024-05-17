package org.araa.controllers;

import lombok.AllArgsConstructor;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.araa.application.dto.Feed;
import org.araa.services.FeedService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;
import java.util.concurrent.CompletableFuture;


@AllArgsConstructor
@RestController
@RequestMapping( "/api/v2/feeds" )
public class FeedController {
    private final FeedService feedService;
    private static final Logger logger = LogManager.getLogger( FeedController.class );

    @GetMapping( "/fetch_feed" )
    public CompletableFuture<ResponseEntity<Feed>> fetchFeed( @RequestParam( "rss" ) String rss ) {
        logger.info( "Received request to fetch feed for {}", rss );
        return feedService.fetchFeed( rss )
                .thenApply( feed -> {
                    logger.info( "Returning feed for {}", rss );
                    return ResponseEntity.ok( feed );
                } )
                .exceptionally( e -> {
                    logger.error( "Error fetching feed", e );
                    return ResponseEntity.status( HttpStatus.INTERNAL_SERVER_ERROR ).build();
                } );
    }

    @GetMapping( "/fetch_all_feeds" )
    public CompletableFuture<ResponseEntity<List<Feed>>> fetchAllFeeds() {
        logger.info( "Received request to fetch all feeds" );
        return feedService.getAllFeedsAsync()
                .thenApply( feeds -> {
                    logger.info( "Returning all feeds" );
                    return ResponseEntity.ok( feeds );
                } )
                .exceptionally( e -> {
                    logger.error( "Error fetching all feeds", e );
                    return ResponseEntity.status( HttpStatus.INTERNAL_SERVER_ERROR ).build();
                } );
    }

}
