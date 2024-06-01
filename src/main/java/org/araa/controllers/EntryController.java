package org.araa.controllers;

import com.rometools.rome.feed.synd.SyndFeed;
import lombok.AllArgsConstructor;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.araa.application.dto.EntryDto;
import org.araa.domain.Entry;
import org.araa.domain.RSS;
import org.araa.domain.User;
import org.araa.infrastructure.utility.XMLParser;
import org.araa.services.AuthService;
import org.araa.services.EntryService;
import org.araa.services.RSSService;
import org.araa.services.UserService;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.web.bind.annotation.*;

import java.time.Duration;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.util.List;
import java.util.Set;
import java.util.concurrent.CompletableFuture;

@AllArgsConstructor
@RestController
@RequestMapping( "/api/v2/entries" )
public class EntryController {
    private EntryService entryService;
    private final AuthService authService;
    private final UserService userService;
    private final RSSService rssService;

    private static final Logger logger = LogManager.getLogger( EntryController.class );

    @GetMapping()
    public Entry fetchEntry( @RequestParam String entryUrl ) {
        return entryService.fetchEntry( entryUrl );
    }

    @PostMapping()
    public Entry saveEntry( Entry entry ) {
        return entryService.saveEntry( entry );
    }


    @GetMapping( "all" )
    public ResponseEntity<List<EntryDto>> fetchEntries( @RequestParam int page, @RequestParam int size ) {
        UserDetails userDetails = authService.getAuthenticatedUser();
        User user = userService.getUserByUsername( userDetails.getUsername() );
        List<Entry> entries = entryService.fetchEntries( user, page, size );
        List<EntryDto> entryDTOs = entries.stream().map( EntryDto::new ).toList();

        CompletableFuture.runAsync( () -> {
            Set<RSS> subscriptions = user.getSubscriptions();
            LocalDateTime now = LocalDateTime.now();
            for ( RSS rss : subscriptions ) {
                LocalDateTime updatedDate = rss.getUpdatedDate().toInstant()
                        .atZone( ZoneId.systemDefault() )
                        .toLocalDateTime();
                if ( Duration.between( updatedDate, now ).toHours() > 10 ) {
                    try {
                        SyndFeed syndFeed = XMLParser.parse( rss.getUrl() );
                        entryService.processEntry( syndFeed, rss, user );
                        rssService.updateRSS( rss );
                    } catch ( Exception e ) {
                        logger.info( "Failed to update RSS {}", rss.getUrl() );
                    }
                }
            }
        } );

        return ResponseEntity.ok( entryDTOs );
    }
}
