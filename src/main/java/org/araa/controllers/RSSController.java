package org.araa.controllers;

import com.rometools.rome.io.FeedException;
import lombok.AllArgsConstructor;
import org.araa.application.dto.RSSDto;
import org.araa.services.RSSService;
import org.hibernate.FetchNotFoundException;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@AllArgsConstructor
@RestController
@RequestMapping( "/api/v2/rss" )
public class RSSController {

    private final RSSService rssService;

    @PostMapping()
    public ResponseEntity<RSSDto> registerRSS( @RequestParam String url ) {
        try {
            RSSDto rssDto = new RSSDto( rssService.registerRSS( url ) );
            return ResponseEntity.ok( rssDto );

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

    @GetMapping("/all")
    public ResponseEntity<List<RSSDto>> fetchAllRSS() {
        List<RSSDto> rssDtos = rssService.getAllRSS();
        return ResponseEntity.ok( rssDtos );
    }
}
