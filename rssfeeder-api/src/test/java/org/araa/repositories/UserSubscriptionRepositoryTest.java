package org.araa.repositories;

import org.araa.domain.RSS;
import org.araa.domain.User;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestInstance;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.jdbc.EmbeddedDatabaseConnection;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.context.SpringBootTest;

import java.util.Date;

import static org.springframework.test.util.AssertionErrors.assertTrue;

@SpringBootTest
@TestInstance( TestInstance.Lifecycle.PER_CLASS )
@AutoConfigureTestDatabase( connection = EmbeddedDatabaseConnection.H2 )
class UserSubscriptionRepositoryTest {
    @Autowired
    UserSubscriptionRepository userSubscriptionRepository;

    @Autowired
    UserRepository userRepository;

    @Autowired
    RSSRepository rssRepository;

    @BeforeAll
    void setUp() {
        User user = User.builder()
                .username( "admin" )
                .password( "admin" )
                .email( "admin@email.com" )
                .name( "admin" )
                .createdDate( new Date() )
                .build();


        RSS rss = RSS.builder()
                .url( "testurl.com" )
                .title( "test" )
                .description( "test" )
                .feedType( "test" )
                .createdDate( new Date() )
                .build();

        userRepository.save( user );
        rssRepository.save( rss );
    }


    @Test
    void subscribe() {
        User user = userRepository.findByUsername( "admin" ).get();
        RSS rss = rssRepository.findByUrl( "testurl.com" );

        userSubscriptionRepository.subscribe( user, rss );

        assertTrue( "User should be subscribed to RSS", userSubscriptionRepository.isSubscribed( user, rss ) );

    }

    @Test
    void unsubscribe() {
        User user = userRepository.findByUsername( "admin" ).get();
        RSS rss = rssRepository.findByUrl( "testurl.com" );

        userSubscriptionRepository.subscribe( user, rss );
        userSubscriptionRepository.unsubscribe( user, rss );

        assertTrue( "User should not be subscribed to RSS", !userSubscriptionRepository.isSubscribed( user, rss ) );
    }

}
