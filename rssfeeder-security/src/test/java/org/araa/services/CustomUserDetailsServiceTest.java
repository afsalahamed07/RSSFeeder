package org.araa.services;

import org.araa.domain.Role;
import org.araa.domain.User;
import org.araa.repositories.UserRepository;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.security.core.userdetails.UserDetails;

import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.when;

@ExtendWith( MockitoExtension.class )
class CustomUserDetailsServiceTest {
    @Mock
    UserRepository userRepository;

    @InjectMocks
    CustomUserDetailsService customUserDetailsService;

    @Test
    void loadUserByUsername_for_existing_user() {
        User user = new User();
        user.setUsername( "test" );
        user.setPassword( "test" );
        user.setEmail( "test@emila.com" );

        Role role = new Role();
        role.setType( "USER" );

        user.setRoles( List.of( role ) );

        when( userRepository.findByUsername( "test" ) ).thenReturn( Optional.of( user ) );

        UserDetails userDetails = customUserDetailsService.loadUserByUsername( "test" );

        assertEquals( "test", userDetails.getUsername() );
        assertEquals( "test", userDetails
                .getPassword() );
        assertEquals( 1, userDetails.getAuthorities().size() );
        assertEquals( "ROLE_USER", userDetails.getAuthorities().stream().findFirst().get().getAuthority() );
    }

    @Test
    void loadUserByUsername_for_non_existing_user() {
        when( userRepository.findByUsername( "test" ) ).thenReturn( Optional.empty() );

        assertThrows( org.springframework.security.core.userdetails.UsernameNotFoundException.class,
                () -> customUserDetailsService.loadUserByUsername( "test" ) );

    }

}