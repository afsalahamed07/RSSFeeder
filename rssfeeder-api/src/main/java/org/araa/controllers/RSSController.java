package org.araa.controllers;

import com.rometools.rome.feed.synd.SyndFeed;
import com.rometools.rome.io.FeedException;
import lombok.AllArgsConstructor;
import org.araa.application.dto.RSSDto;
import org.araa.domain.Entry;
import org.araa.domain.RSS;
import org.araa.domain.User;
import org.araa.infrastructure.utility.XMLParser;
import org.araa.services.AuthService;
import org.araa.services.EntryService;
import org.araa.services.RSSService;
import org.araa.services.UserService;
import org.hibernate.FetchNotFoundException;
import org.springframework.data.redis.cache.RedisCacheManager;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Objects;
import java.util.concurrent.CompletableFuture;

@AllArgsConstructor
@RestController
@RequestMapping( "/api/v2/rss" )
public class RSSController {
    private final RSSService rssService;
    private final UserService userService;
    private final EntryService entryService;
    private final AuthService authService;
    private final RedisCacheManager cacheManager;


    @PostMapping()
    public ResponseEntity<RSSDto> registerRSS( @RequestParam String url ) {
        try {
            UserDetails userDetails = authService.getAuthenticatedUser();
            User user = userService.getUserByUsername( userDetails.getUsername() );

            SyndFeed syndFeed = XMLParser.parse( url );
            RSS rss = rssService.registerRSS( syndFeed );

            Objects.requireNonNull( cacheManager.getCache( "entries" ) ).clear();

            // Asynchronous calls
            CompletableFuture<List<Entry>> entries = entryService.processEntry( syndFeed, rss );
            userService.updateEntries( user, entries );
            userService.subscribeRSS( user, rss );

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
