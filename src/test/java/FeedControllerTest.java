import com.rometools.rome.feed.synd.SyndFeed;
import org.araa.application.builder.FeedFactory;
import org.araa.controllers.FeedController;
import org.araa.application.dto.Feed;
import org.araa.infrastructure.utility.XMLParser;
import org.araa.services.FeedService;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.http.ResponseEntity;

import java.util.concurrent.CompletableFuture;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
public class FeedControllerTest {
    @Mock
    private FeedService feedService;

    @Mock
    private XMLParser XMLParser;

    @Mock
    private FeedFactory feedFactory;

    @InjectMocks
    private FeedController feedController;

    @Test
    public void testFetchFeed() throws Exception{
        SyndFeed syndFeed = XMLParser.parse("https://www.newswire.lk/feed");
        Feed mockFeed = feedFactory.createFeed(syndFeed);
        when(feedService.fetchFeed("https://www.newswire.lk/feed")).thenReturn(CompletableFuture.completedFuture(mockFeed));

        CompletableFuture<ResponseEntity<Feed>> response = feedController.fetchFeed("https://www.newswire.lk/feed");

        assertTrue(response.join().getStatusCode().is2xxSuccessful());
        assertEquals(mockFeed, response.join().getBody());


        verify(feedService).fetchFeed("https://www.newswire.lk/feed");
    }
}
