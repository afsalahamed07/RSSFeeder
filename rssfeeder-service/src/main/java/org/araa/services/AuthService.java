package org.araa.services;

import lombok.AllArgsConstructor;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Service;
import utility.JWTGenerator;

@AllArgsConstructor
@Service
public class AuthService {
    private static final Logger logger = LogManager.getLogger( AuthService.class );

    private final AuthenticationManager authenticationManager;
    private JWTGenerator jwtGenerator;

    public String authenticate( String userName, String password ) {
        Authentication authentication = authenticationManager.authenticate(
                new UsernamePasswordAuthenticationToken( userName, password )
        );
        SecurityContextHolder.getContext().setAuthentication( authentication );

        return jwtGenerator.generateToken( authentication );
    }

    public String getUsername() {
        UserDetails userDetails = ( UserDetails ) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        if ( SecurityContextHolder.getContext().getAuthentication() == null ) {
            logger.error( "User not authenticated" );
            throw new AuthenticationException( "User not authenticated" ) {
            };
        }
        return userDetails.getUsername();
    }
}
