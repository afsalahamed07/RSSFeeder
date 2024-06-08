package org.araa.services;

import lombok.AllArgsConstructor;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.araa.application.dto.AuthResponseDTO;
import org.araa.application.dto.LoginCredentialsDto;
import org.araa.application.security.JWTGenerator;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Service;

@AllArgsConstructor
@Service
public class AuthService {
    private static final Logger logger = LogManager.getLogger( AuthService.class );

    private final AuthenticationManager authenticationManager;
    private JWTGenerator jwtGenerator;

    public AuthResponseDTO authenticate( LoginCredentialsDto loginCredentialsDto ) {
        Authentication authentication = authenticationManager.authenticate(
                new UsernamePasswordAuthenticationToken( loginCredentialsDto.getUsername(),
                        loginCredentialsDto.getPassword() )
        );
        SecurityContextHolder.getContext().setAuthentication( authentication );

        String token = jwtGenerator.generateToken( authentication );

        return new AuthResponseDTO( token );
    }

    public UserDetails getAuthenticatedUser() {
        UserDetails userDetails = ( UserDetails ) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        if ( SecurityContextHolder.getContext().getAuthentication() == null ) {
            logger.error( "User not authenticated" );
            throw new AuthenticationException( "User not authenticated" ) {
            };
        }

        return userDetails;
    }
}
