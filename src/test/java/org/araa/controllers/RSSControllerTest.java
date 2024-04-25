package org.araa.controllers;

import org.araa.application.builder.FeedFactory;
import org.araa.application.dto.Feed;
import org.araa.application.dto.RSSDto;
import org.araa.infrastructure.utility.XMLParser;
import org.araa.services.FeedService;
import org.araa.services.RSSService;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.http.ResponseEntity;

import java.util.concurrent.CompletableFuture;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.when;

@ExtendWith( MockitoExtension.class)
class RSSControllerTest {

    @Mock
    private RSSService rssService;

    @InjectMocks
    private RSSController rssController;

    @Test
    void registerRSS() {
        RSSDto rssDto = rssService.registerRSS( "https://www.newswire.lk/feed" );

        CompletableFuture<ResponseEntity<RSSDto>> response = rssController.registerRSS("https://www.newswire.lk/feed");

        assertNotNull( response.join().getBody() );
        assertEquals( rssDto, response.join().getBody() );
    }
}