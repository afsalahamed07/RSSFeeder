package org.araa.services;

import lombok.AllArgsConstructor;
import lombok.NoArgsConstructor;
import org.araa.domain.Role;
import org.araa.domain.User;
import org.araa.repositories.UserRepository;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import java.util.List;

@AllArgsConstructor
@NoArgsConstructor
@Service
public class CustomUserDetailsService implements UserDetailsService {
    private UserRepository userRepository;

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
}
