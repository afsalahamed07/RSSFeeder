package org.araa.controllers;

import com.rometools.rome.feed.synd.SyndFeed;
import com.rometools.rome.io.FeedException;
import lombok.AllArgsConstructor;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.araa.application.dto.RSSDto;
import org.araa.domain.RSS;
import org.araa.domain.User;
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

@AllArgsConstructor
@RestController
@RequestMapping( "/api/v2/rss" )
public class RSSController {

    private static final Logger logger = LogManager.getLogger( RSSController.class );

    private final RSSService rssService;
    private final AuthService authService;
    private final UserService userService;
    private final EntryService entryService;

    @PostMapping()
    public ResponseEntity<RSSDto> registerRSS( @RequestParam String url ) {
        logger.info( "Registering RSS from {}", url );
        UserDetails userDetails = authService.getAuthenticatedUser();
        User user = userService.getUserByUsername( userDetails.getUsername() );

        try {
            SyndFeed syndFeed = XMLParser.parse( url );
            RSS rss = rssService.from( syndFeed );
            rss = rssService.save( rss );

            // Asynchronous calls
            entryService.processEntry( syndFeed, rss, user );
            userService.subscribeRSS( user, rss );
            rssService.updateRSS( rss ); // flag keep track of last entries extraction

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
