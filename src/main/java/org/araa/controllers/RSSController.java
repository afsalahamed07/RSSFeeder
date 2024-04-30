package org.araa.controllers;

import com.rometools.rome.io.FeedException;
import lombok.AllArgsConstructor;
import org.araa.application.dto.RSSDto;
import org.araa.domain.RSS;
import org.araa.services.RSSService;
import org.hibernate.FetchNotFoundException;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.Set;
import java.util.stream.Collectors;

@AllArgsConstructor
@RestController
@RequestMapping( "/api/rss" )
public class RSSController {

    private final RSSService rssService;

    @PostMapping( "/register_rss" )
    public ResponseEntity<RSSDto> registerRSS( @RequestParam String url ) {
        try {
            RSSDto rssDto = new RSSDto( rssService.registerRSS( url ) );
            return ResponseEntity.ok( rssDto );

        } catch ( FeedException e ) {
            return ResponseEntity.badRequest().build();
        }
    }

    @GetMapping( "/fetch_rss" )
    public ResponseEntity<RSSDto> fetchRSS( @RequestParam String url ) {
        try {
            RSSDto rssDto = new RSSDto( rssService.getRSS( url ) );
            return ResponseEntity.ok( rssDto );

        } catch ( FetchNotFoundException e ) {
            return ResponseEntity.badRequest().build();
        }
    }
}
