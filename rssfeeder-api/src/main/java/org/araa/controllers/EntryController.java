package org.araa.controllers;

import com.rometools.rome.feed.synd.SyndFeed;
import lombok.AllArgsConstructor;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.araa.domain.Entry;
import org.araa.domain.RSS;
import org.araa.domain.User;
import org.araa.dto.EntriesDTO;
import org.araa.dto.EntryDTO;
import org.araa.services.AuthService;
import org.araa.services.EntryService;
import org.araa.services.RSSService;
import org.araa.services.UserService;
import org.araa.utility.XMLParser;
import org.springframework.http.ResponseEntity;
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
    private static final Logger logger = LogManager.getLogger( EntryController.class );
    private EntryService entryService;
    private final AuthService authService;
    private final UserService userService;
    private RSSService rssService;

    @GetMapping()
    public Entry fetchEntry( @RequestParam String entryUrl ) {
        return entryService.fetchEntry( entryUrl );
    }

    @GetMapping( "all" )
    public ResponseEntity<EntriesDTO> fetchEntries( @RequestParam int page, @RequestParam int size ) {
        if ( page < 0 || size < 0 ) {
            return ResponseEntity.badRequest().build();
        }

        String username = authService.getUsername();
        User user = userService.getUserByUsername( username );
        List<Entry> entries = entryService.pagedEntries( page, size, user );

        List<EntryDTO> entryDTOs = entries.stream().map( EntryDTO::new ).toList();

        EntriesDTO entriesDTO = EntriesDTO.builder()
                .entries( entryDTOs )
                .page( page )
                .size( size )
                .totalPages( entryService.totalPages( size, entryService.entries( user.getId() ) ) )
                .totalElements( entryService.entries( user.getId() ) )
                .nextPage( page + 1 )
                .previousPage( page - 1 )
                .build();

        CompletableFuture.runAsync( () -> {
            LocalDateTime now = LocalDateTime.now();
            Set<RSS> subscriptions = userService.getUserSubscriptions( user );
            for ( RSS rss : subscriptions ) {
                LocalDateTime updatedDate =
                        rss.getUpdatedDate().toInstant().atZone( ZoneId.systemDefault() ).toLocalDateTime();
                if ( Duration.between( updatedDate, now ).toHours() > 1 ) {
                    try {
                        SyndFeed syndFeed = XMLParser.parse( rss.getUrl() );
                        CompletableFuture<List<Entry>> futureEntries = entryService.processEntry( syndFeed, rss );
                        userService.updateEntries( user, futureEntries );
                        rssService.updateRSS( rss );
                    } catch ( Exception e ) {
                        logger.info( "Failed to update RSS {}", rss.getUrl() );
                    }
                }
            }
        } );

        return ResponseEntity.ok( entriesDTO );
    }
}
