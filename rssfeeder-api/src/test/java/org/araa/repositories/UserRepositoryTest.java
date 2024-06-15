package org.araa.repositories;

import org.araa.domain.Entry;
import org.araa.domain.User;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestInstance;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.jdbc.EmbeddedDatabaseConnection;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;

import java.util.Set;

import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertTrue;

@DataJpaTest
@TestInstance( TestInstance.Lifecycle.PER_CLASS )
@AutoConfigureTestDatabase( connection = EmbeddedDatabaseConnection.H2 )
class UserRepositoryTest {

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private EntryRepository entryRepository;

    @BeforeAll
    void setUp() {
        assertNotNull( userRepository );

        User user = new User();
        user.setUsername( "admin" );
        user.setPassword( "admin" );
        user.setEmail( "admin@email.com" );
        user.setName( "admin" );
        userRepository.save( user );

        // create a row in user_entry
        Entry entry = new Entry();
        entry.setLink( "testlink.com" );
        entryRepository.save( entry );

        user.setEntries( Set.of( entry ) );
        userRepository.save( user );
    }

    @Test
    void findByUsername() {
        assertNotNull( userRepository.findByUsername( "admin" ) );
    }

    @Test
    void existsByUsername() {
        assertNotNull( userRepository.existsByUsername( "admin" ) );
    }

    @Test
    void existsByEntry() {
        assertTrue( userRepository.existsByEntry( userRepository.findByUsername( "admin" ).get().getId(),
                entryRepository.findByLink( "testlink.com" ).get().getId() ) );
    }

}