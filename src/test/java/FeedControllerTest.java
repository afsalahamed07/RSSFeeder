import org.araa.feed.Feed;
import org.araa.feed.FeedController;
import org.araa.feed.FeedFetcher;
import org.araa.feed.FeedService;
import org.jdom2.Document;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.http.ResponseEntity;

import java.util.Objects;
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

    private final FeedFetcher feedFetcher = FeedFetcher.INSTANCE;

    @Test
    public void testFetchFeed() throws Exception{
        Document document = feedFetcher.parseFeedAsync("https://www.newswire.lk/feed").join();
        Feed mockFeed = new Feed(document);
        when(feedService.fetchFeed("https://www.newswire.lk/feed")).thenReturn(CompletableFuture.completedFuture(mockFeed));

        CompletableFuture<ResponseEntity<Feed>> response = feedController.fetchFeed("https://www.newswire.lk/feed");

        assertTrue(response.join().getStatusCode().is2xxSuccessful());
        assertEquals(mockFeed, response.join().getBody());


        verify(feedService).fetchFeed("https://www.newswire.lk/feed");
    }
}
