package org.araa.controllers;

import com.rometools.rome.io.FeedException;
import lombok.AllArgsConstructor;
import org.araa.application.dto.RSSDto;
import org.araa.services.RSSService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

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
}
