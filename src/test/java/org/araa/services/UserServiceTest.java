package org.araa.services;

import org.araa.domain.RSS;
import org.araa.domain.User;
import org.araa.repositories.UserRepository;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentCaptor;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.security.core.userdetails.UsernameNotFoundException;

import java.util.HashSet;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@ExtendWith( MockitoExtension.class )
class UserServiceTest {
    @Mock
    UserRepository userRepository;

    @InjectMocks
    UserService userService;

    @Test
    void registerUser() {
    }

    @Test
    void loadUserByUsername() {
    }

    @Test
    void subscribeRSS_UserExists() {
        // Prepare mock data
        String username = "testuser";
        RSS mockRSS = new RSS();
        mockRSS.setUrl( "https://www.newswire.lk/feed" );
        mockRSS.setTitle( "Newswire" );

        User mockUser = new User();
        mockUser.setUsername( username );
        mockUser.setSubscriptions( new HashSet<>() );

        // Mock repository methods
        when( userRepository.findByUsername( anyString() ) ).thenReturn( Optional.of( mockUser ) );
        when( userRepository.save( any( User.class ) ) ).thenReturn( mockUser );

        // Call the method under test
        userService.subscribeRSS( username, mockRSS );

        // Capture the argument passed to save method
        ArgumentCaptor<User> userCaptor = ArgumentCaptor.forClass( User.class );
        verify( userRepository ).save( userCaptor.capture() );

        // Verify the user subscriptions
        User savedUser = userCaptor.getValue();
        assertEquals( 1, savedUser.getSubscriptions().size() );
        assertEquals( mockRSS, savedUser.getSubscriptions().iterator().next() );
    }

    @Test
    void subscribeRSS_UserNotFound() {
        // Prepare mock data
        String username = "nonexistentuser";
        RSS mockRSS = new RSS();
        mockRSS.setUrl( "https://www.newswire.lk/feed" );
        mockRSS.setTitle( "Newswire" );

        // Mock repository methods
        when( userRepository.findByUsername( anyString() ) ).thenReturn( Optional.empty() );

        // Call the method under test and expect an exception
        assertThrows( UsernameNotFoundException.class, () -> {
            userService.subscribeRSS( username, mockRSS );
        } );
    }

    @Test
    void setUserRole() {

    }

    @Test
    void getUserSubscriptions() {
    }

    @Test
    void getUserByUsername() {
    }
}