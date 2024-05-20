package org.araa.controllers;

import com.rometools.rome.io.FeedException;
import lombok.AllArgsConstructor;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.araa.application.dto.RSSDto;
import org.araa.domain.RSS;
import org.araa.services.AuthService;
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

    @PostMapping()
    public ResponseEntity<RSSDto> registerRSS( @RequestParam String url ) {
        UserDetails userDetails = authService.getAuthenticatedUser();

        try {
            RSS rss = rssService.registerRSS( url );

            CompletableFuture.runAsync( () -> {
                try {
                    userService.subscribeRSS( userDetails.getUsername(), rss );
                } catch ( FetchNotFoundException e ) {
                    logger.info( "Failed to subscribe RSS for user {}", userDetails.getUsername() );
                }
            } );

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
        List<RSSDto> rssDtos = rssService.getAllRSS();
        return ResponseEntity.ok( rssDtos );
    }
}
