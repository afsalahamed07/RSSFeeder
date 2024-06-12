package org.araa.services;

import org.araa.domain.User;
import org.araa.error.UserAlreadyExistError;
import org.araa.repositories.UserRepository;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.security.crypto.password.PasswordEncoder;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;

@ExtendWith( MockitoExtension.class )
class CustomUserDetailsServiceTest {
    @Mock
    UserRepository userRepository;

    @Mock
    PasswordEncoder passwordEncoder;

    @InjectMocks
    CustomUserDetailsService customUserDetailsService;

    @Test
    void save_new_user() {
        User user = new User();
        user.setUsername( "testuser" );
        user.setEmail( "test@email.com" );
        user.setName( "Test User" );
        user.setPassword( "password" );

        when( passwordEncoder.encode( user.getPassword() ) ).thenReturn( user.getPassword() );
        when( userRepository.save( any( User.class ) ) ).thenReturn( user );

        User savedUser = customUserDetailsService.registerUser( user.getUsername(), user.getName(), user.getEmail(),
                user.getPassword() );

        assertNotNull( savedUser );
        assertEquals( user.getUsername(), savedUser.getUsername() );
        assertEquals( user.getEmail(), savedUser.getEmail() );
        assertEquals( user.getName(), savedUser.getName() );
        assertEquals( user.getPassword(), savedUser.getPassword() );
    }

    @Test
    void save_existing_user() {
        User user = new User();
        user.setUsername( "testuser" );
        user.setEmail( "test@email.com" );
        user.setName( "Test User" );
        user.setPassword( "password" );

        when( passwordEncoder.encode( user.getPassword() ) ).thenReturn( user.getPassword() );
        when( userRepository.existsByUsername( user.getUsername() ) ).thenReturn( Boolean.TRUE );

        assertThrowsExactly( UserAlreadyExistError.class, () ->
                customUserDetailsService.registerUser( user.getUsername(), user.getName(), user.getEmail(), user.getPassword() )
        );
    }
}