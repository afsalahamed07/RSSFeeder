//package org.araa.services;
//
//import com.rometools.rome.io.FeedException;
//import org.araa.domain.RSS;
//import org.araa.repositories.RSSRepository;
//import org.junit.jupiter.api.Disabled;
//import org.junit.jupiter.api.Test;
//import org.junit.jupiter.api.extension.ExtendWith;
//import org.mockito.InjectMocks;
//import org.mockito.Mock;
//import org.mockito.Mockito;
//import org.mockito.junit.jupiter.MockitoExtension;
//
//import java.util.List;
//
//import static org.junit.jupiter.api.Assertions.assertEquals;
//import static org.junit.jupiter.api.Assertions.assertNotNull;
//import static org.mockito.ArgumentMatchers.any;
//import static org.mockito.ArgumentMatchers.anyString;
//import static org.mockito.Mockito.when;
//
//@ExtendWith( MockitoExtension.class )
//class RSSServiceTest {
//    @Mock
//    RSSRepository rssRepository;
//    @InjectMocks
//    RSSService rssService;
//
//    @Disabled( "Will be written to adhere to the new design" )
//    @Test
//    void save_RSS_New_RSS_URL_Returns_New_Saved_RSS() {
////        try {
////            RSS mockRSS = RSS.builder()
////                    .url( "https://www.newswire.lk/feed" )
////                    .title( "Newswire" )
////                    .build();
////
////            when( rssRepository.existsByUrl( anyString() ) ).thenReturn( false );
////            when( rssRepository.save( any( RSS.class ) ) ).thenReturn( mockRSS );
////
////            RSS rss = rssService.save( "https://www.newswire.lk/feed" );
////
////            assertNotNull( rss );
////            assertEquals( mockRSS.getUrl(), rss.getUrl() );
////            assertEquals( mockRSS.getTitle(), rss.getTitle() );
////        } catch ( FeedException e ) {
////            throw new RuntimeException( e );
////        }
//    }
//
//    @Disabled( "Will be written to adhere to the new design" )
//    @Test
//    void save_RSS_Existing_RSS_URL_Returns_Existing_RSS() {
////        try {
////            RSS mockRSS = RSS.builder()
////                    .url( "https://www.newswire.lk/feed" )
////                    .title( "Newswire" )
////                    .build();
////
////            when( rssRepository.existsByUrl( anyString() ) ).thenReturn( true );
////            when( rssRepository.findByUrl( anyString() ) ).thenReturn( mockRSS );
////
////            RSS rss = rssService.save( "https://www.newswire.lk/feed" );
////
////            assertNotNull( rss );
////            assertEquals( mockRSS.getUrl(), rss.getUrl() );
////            assertEquals( mockRSS.getTitle(), rss.getTitle() );
////        } catch ( FeedException e ) {
////            throw new RuntimeException( e );
////        }
//    }
//
//    @Test
//    void getRSS() {
//        RSS mockRss = Mockito.mock( RSS.class );
//        when( rssRepository.existsByUrl( anyString() ) ).thenReturn( true );
//        when( rssRepository.findByUrl( anyString() ) ).thenReturn( mockRss );
//
//        RSS rss = rssService.getRSS( "https://www.newswire.lk/feed" );
//
//        assertNotNull( rss );
//    }
//
//    @Test
//    void getAllRSS() {
//        RSS mockRss = Mockito.mock( RSS.class );
//        when( rssRepository.findAll() ).thenReturn( List.of( mockRss ) );
//
//        List<RSS> rssList = rssService.getAllRSS();
//
//        assertNotNull( rssList );
//        assertEquals( 1, rssList.size() );
//    }
//}