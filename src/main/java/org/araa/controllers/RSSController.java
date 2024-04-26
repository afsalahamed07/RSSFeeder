package org.araa.controllers;

import lombok.AllArgsConstructor;
import org.araa.application.dto.RSSDto;
import org.araa.domain.RSS;
import org.araa.services.RSSService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.concurrent.CompletableFuture;

@AllArgsConstructor
@RestController
@RequestMapping( "/api/rss" )
public class RSSController {

    private final RSSService rssService;

    @PostMapping( "/register_rss" )
    public ResponseEntity<RSSDto> registerRSS( @RequestParam String url ) {
        return ResponseEntity.ok( rssService.registerRSS( url ) );
    }
}
