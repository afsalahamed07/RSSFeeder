package org.araa.services;

import lombok.AllArgsConstructor;
import org.araa.domain.Role;
import org.araa.domain.User;
import org.araa.error.UserAlreadyExistError;
import org.araa.repositories.UserRepository;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.Date;
import java.util.List;

@AllArgsConstructor
@Service
public class CustomUserDetailsService implements UserDetailsService {
    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;

    @Override
    public UserDetails loadUserByUsername( String username ) throws UsernameNotFoundException {
        User user = userRepository.findByUsername( username )
                .orElseThrow( () -> new UsernameNotFoundException( username + " not found" ) );

        return new org.springframework.security.core.userdetails.
                User( user.getUsername(), user.getPassword(), mapRolesToAuthorities( user.getRoles() ) );
    }

    private List<SimpleGrantedAuthority> mapRolesToAuthorities( List<Role> roles ) {
        return roles.stream()
                .map( role -> new SimpleGrantedAuthority( "ROLE_" + role.getType() ) )
                .toList();
    }


    public User registerUser( String userName, String name, String email, String password ) throws UserAlreadyExistError {
        User user = User.builder()
                .username( userName )
                .name( name )
                .email( email )
                    .password( passwordEncoder.encode( password ) )
                .createdDate( new Date() )
                .build();

        // todo: this is a temporary fix
        Role role = new Role();
        role.setType( "USER" );


        user.setRole( role ); // error due to the role is transient

        user = save( user ); // throws UserAlreadyExistError

        return user;
    }

    private User save( User user ) {
        if ( Boolean.TRUE.equals( userRepository.existsByUsername( user.getUsername() ) ) ) {
            throw new UserAlreadyExistError( "Username already exists" );
        }
        return userRepository.save( user );
    }
}
