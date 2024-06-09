package org.araa.services;

import errors.UserAlreadyExistError;
import org.araa.domain.User;
import org.araa.repositories.UserRepository;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;

@ExtendWith( MockitoExtension.class )
class UserServiceTest {

    @Mock
    UserRepository userRepository;

    @Test
    void save_new_user() {
        User user = new User();
        user.setUsername( "testuser" );
        user.setEmail( "test@email.com" );
        user.setName( "Test User" );
        user.setPassword( "password" );

        when( userRepository.save( any( User.class ) ) ).thenReturn( user );

        User savedUser = userRepository.save( user );

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

        when( userRepository.existsByUsername( user.getUsername() ) ).thenReturn( true );

        assertThrowsExactly( UserAlreadyExistError.class, () -> userRepository.save( user ) );
    }
}