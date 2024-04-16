import org.araa.config.RedisCacheConfig;
import org.araa.feed.Feed;
import org.araa.feed.FeedController;
import org.araa.feed.FeedService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.MockitoAnnotations;
import org.springframework.cache.CacheManager;
import org.springframework.data.redis.connection.RedisConnectionFactory;
import org.springframework.data.redis.connection.lettuce.LettuceConnectionFactory;
import org.springframework.http.ResponseEntity;

import java.util.concurrent.CompletableFuture;

import static org.junit.jupiter.api.Assertions.assertTrue;

public class FeedControllerTest {
    @Mock
    private FeedService feedService;

    @InjectMocks
    private FeedController feedController;

    @Test
    public void testFetchFeed() throws Exception{
        Feed mockFeed = new Feed();


    }
}
