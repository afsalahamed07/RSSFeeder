package org.araa.services;

import jakarta.persistence.EntityManagerFactory;
import org.araa.domain.RSS;
import org.araa.repositories.UserRepository;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.UUID;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;

@ExtendWith( MockitoExtension.class )
class UserServiceTest {

    @Mock
    UserRepository userRepository;

    @InjectMocks
    UserService userService;


    @Test
    void subscribe_to_new_rss() {
        RSS rss = new RSS();

        when( userRepository.existsByEntry( any( UUID.class ), any( UUID.class ) ) ).thenReturn( false );


    }

    @Test
    void getUserSubscriptions() {
    }

    @Test
    void getUserByUsername() {
    }

    @Test
    void updateEntries() {
    }
}