package org.araa.repositories;

import org.araa.domain.User;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestInstance;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.jdbc.EmbeddedDatabaseConnection;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.context.annotation.Import;

@DataJpaTest
@Import( TestConfig.class )
@TestInstance( TestInstance.Lifecycle.PER_CLASS )
@AutoConfigureTestDatabase( connection = EmbeddedDatabaseConnection.H2 )
class UserRepositoryImpTest {
    // the autowiring failing
    @Autowired
    private UserRepository userRepository;

    // before populate the database
    @BeforeAll
    void setUp() {
        // populate the db with test user and test role
        User user = User.builder()
                .username( "testuser" )
                .password( "password" )
                .email( "test@email.com" )
                .name( "Test User" )
                .build();

        userRepository.save( user );
    }

    @Test
    void subscribe() {
    }
}