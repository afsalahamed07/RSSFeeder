package org.araa.services;

import com.rometools.rome.io.FeedException;
import org.araa.domain.RSS;
import org.araa.repositories.RSSRepository;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.when;

@ExtendWith( MockitoExtension.class )
class RSSServiceTest {
    @Mock
    RSSRepository rssRepository;
    @InjectMocks
    RSSService rssService;

    @Test
    void registerRSS() {
        try {
            RSS mockRSS = new RSS();
            mockRSS.setUrl( "https://www.newswire.lk/feed" );
            mockRSS.setTitle( "Newswire" );

            // Mocking the behavior of rssRepository.existsByUrl
            when( rssRepository.existsByUrl( anyString() ) ).thenReturn( false );
            // Mocking the behavior of rssRepository.save
            when( rssRepository.save( any( RSS.class ) ) ).thenReturn( mockRSS );

            RSS rss = rssService.registerRSS( "https://www.newswire.lk/feed" );

            assertEquals( mockRSS.getUrl(), rss.getUrl() );
            assertEquals( mockRSS.getTitle(), rss.getTitle() );
        } catch ( FeedException e ) {
            throw new RuntimeException( e );
        }
    }

    @Test
    void getRSS() {
    }

    @Test
    void getAllRSS() {
        // todo: finish this
    }
}