import org.araa.domain.Feed;
import org.araa.controllers.FeedController;
import org.araa.infrastructure.utility.FetchDocument;
import org.araa.services.FeedService;
import org.jdom2.Document;
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

    @InjectMocks
    private FeedController feedController;

    private final FetchDocument fetchDocument = FetchDocument.INSTANCE;

    @Test
    public void testFetchFeed() throws Exception{
        Document document = fetchDocument.fetchAndParseFeed("https://www.newswire.lk/feed");
        Feed mockFeed = new Feed(document);
        when(feedService.fetchFeed("https://www.newswire.lk/feed")).thenReturn(CompletableFuture.completedFuture(mockFeed));

        CompletableFuture<ResponseEntity<Feed>> response = feedController.fetchFeed("https://www.newswire.lk/feed");

        assertTrue(response.join().getStatusCode().is2xxSuccessful());
        assertEquals(mockFeed, response.join().getBody());


        verify(feedService).fetchFeed("https://www.newswire.lk/feed");
    }
}
