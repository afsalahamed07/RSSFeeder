package org.araa.services;

import jakarta.transaction.Transactional;
import lombok.AllArgsConstructor;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.araa.application.dto.UserRegistrationDto;
import org.araa.application.dto.UserRegistrationResponseDTO;
import org.araa.application.error.UserAlreadyExistError;
import org.araa.domain.Entry;
import org.araa.domain.RSS;
import org.araa.domain.Role;
import org.araa.domain.User;
import org.araa.repositories.UserRepository;
import org.springframework.scheduling.annotation.Async;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.Collections;
import java.util.Date;
import java.util.List;
import java.util.Set;

@AllArgsConstructor
@Service
public class UserService implements UserDetailsService {

    private static final Logger logger = LogManager.getLogger( UserService.class );
    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;
    private final RoleService roleService;

    public UserRegistrationResponseDTO registerUser( UserRegistrationDto userRegistrationDto ) throws UserAlreadyExistError {
        if ( Boolean.TRUE.equals( userRepository.existsByUsername( userRegistrationDto.getUsername() ) ) ) {
            throw new UserAlreadyExistError( "Username already exists" );
        }

        User user = User.builder()
                .username( userRegistrationDto.getUsername() )
                .name( userRegistrationDto.getName() )
                .email( userRegistrationDto.getEmail() )
                .password( passwordEncoder.encode( userRegistrationDto.getPassword() ) )
                .createdDate( new Date() )
                .build();

        setUserRole( user ); // todo: Need refactoring

        user = userRepository.save( user );

        return new UserRegistrationResponseDTO( user );
    }

    @Override
    public UserDetails loadUserByUsername( String username ) throws UsernameNotFoundException {
        User user = userRepository.findByUsername( username )
                .orElseThrow( () -> new UsernameNotFoundException( username + " not found" ) );

        return new org.springframework.security.core.userdetails.User( user.getUsername(), user.getPassword(), mapRolesToAuthorities( user.getRoles() ) );
    }


    @Async
    @Transactional
    public void subscribeRSS( User user, RSS rss ) {
        user.getSubscriptions().add( rss );
        userRepository.subscribeToRss( user.getId(), rss.getId() );
        logger.info( "User {} subscribed to RSS {}", user.getUsername(), rss.getUrl() );
    }

    private List<SimpleGrantedAuthority> mapRolesToAuthorities( List<Role> roles ) {
        return roles.stream()
                .map( role -> new SimpleGrantedAuthority( "ROLE_" + role.getType() ) )
                .toList();
    }


    public void setUserRole( User user ) {
        Role roles = roleService.getRole( "USER" );
        user.setRoles( Collections.singletonList( roles ) );
    }

    public Set<RSS> getUserSubscriptions( String username ) {
        User user = userRepository.findByUsername( username )
                .orElseThrow( () -> new UsernameNotFoundException( "User not found" ) );
        return user.getSubscriptions();
    }

    public User getUserByUsername( String username ) {
        return userRepository.findByUsername( username )
                .orElseThrow( () -> new UsernameNotFoundException( "User not found" ) );
    }

    public User save( User user ) {
        return userRepository.save( user );
    }

    public void saveEntry( User user, Entry entry ) {
        if ( userRepository.existsByEntry( user.getId(), entry.getId() ) ) {
            logger.info( "Entry {} already saved to user {}", entry.getId(), user.getId() );
            return;
        }
        try {
            userRepository.addEntryToUser( user.getId(), entry.getId() );
            logger.info( "Entry {} saved to user {}", entry.getId(), user.getId() );
        } catch ( Exception e ) {
            logger.error( "Error saving entry to user", e );
        }
    }
}