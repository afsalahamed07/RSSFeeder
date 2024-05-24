package org.araa.controllers;

import com.rometools.rome.feed.synd.SyndEntry;
import com.rometools.rome.feed.synd.SyndFeed;
import com.rometools.rome.io.FeedException;
import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;
import lombok.AllArgsConstructor;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.araa.application.dto.RSSDto;
import org.araa.domain.Entry;
import org.araa.domain.RSS;
import org.araa.infrastructure.utility.XMLParser;
import org.araa.services.AuthService;
import org.araa.services.EntryService;
import org.araa.services.RSSService;
import org.araa.services.UserService;
import org.hibernate.FetchNotFoundException;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.concurrent.CompletableFuture;

@AllArgsConstructor
@RestController
@RequestMapping( "/api/v2/rss" )
public class RSSController {

    private static final Logger logger = LogManager.getLogger( RSSController.class );

    private final RSSService rssService;
    private final AuthService authService;
    private final UserService userService;
    private final EntryService entryService;
    @PersistenceContext
    private final EntityManager entityManager;

    @PostMapping()
    public ResponseEntity<RSSDto> registerRSS( @RequestParam String url ) {
        UserDetails userDetails = authService.getAuthenticatedUser();

        try {
            SyndFeed syndFeed = XMLParser.parse( url );
            RSS rss = rssService.from( syndFeed, url );
            rss = rssService.registerRSS( rss );

            for ( SyndEntry syndEntry : syndFeed.getEntries() ) {
                CompletableFuture<Entry> savedEntry = entryService.processEntry( syndEntry, rss );
                userService.subscribeEntry( userDetails.getUsername(), savedEntry );
            }

            userService.subscribeRSS( userDetails.getUsername(), rss );
            rssService.updateRSS( rss );

            return ResponseEntity.ok( new RSSDto( rss ) );
        } catch ( FeedException e ) {
            return ResponseEntity.badRequest().build();
        }
    }

    @GetMapping()
    public ResponseEntity<RSSDto> fetchRSS( @RequestParam String url ) {
        try {
            RSSDto rssDto = new RSSDto( rssService.getRSS( url ) );
            return ResponseEntity.ok( rssDto );

        } catch ( FetchNotFoundException e ) {
            return ResponseEntity.badRequest().build();
        }
    }

    @GetMapping( "/all" )
    public ResponseEntity<List<RSSDto>> fetchAllRSS() {
        List<RSS> rss = rssService.getAllRSS();
        return ResponseEntity.ok( RSSDto.from( rss ) );
    }
}
