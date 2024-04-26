package org.araa.infrastructure.config.security;

import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.araa.application.security.JWTGenerator;
import org.araa.services.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.web.authentication.WebAuthenticationDetailsSource;
import org.springframework.security.web.context.HttpSessionSecurityContextRepository;
import org.springframework.security.web.context.SecurityContextRepository;
import org.springframework.util.StringUtils;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;

public class JWTAuthenticationFilter extends OncePerRequestFilter {

    private static final Logger logger = LogManager.getLogger( JWTAuthenticationFilter.class );

    @Autowired
    private JWTGenerator jwtGenerator;
    @Autowired
    private UserService userService;


    @Override
    protected void doFilterInternal( HttpServletRequest request, HttpServletResponse response, FilterChain filterChain ) throws ServletException, IOException {
        logger.info( "Request received to authenticate user" );
        String token = getJWTFromRequest( request );
        logger.info( "Token received: {}", token );

        if ( StringUtils.hasText( token ) && jwtGenerator.validateToken( token ) ) {
            String username = jwtGenerator.getUsernameFromJWT( token );
            UserDetails userDetails = userService.loadUserByUsername( username );
            UsernamePasswordAuthenticationToken authenticationToken = new UsernamePasswordAuthenticationToken( userDetails,
                    null,
                    userDetails.getAuthorities() );
            authenticationToken.setDetails( new WebAuthenticationDetailsSource().buildDetails( request ) );
            SecurityContextHolder.getContext().setAuthentication( authenticationToken );
            logger.info( "User authenticated" );
        }

        filterChain.doFilter( request, response );
    }

    private String getJWTFromRequest( HttpServletRequest request ) {
        String bearerToken = request.getHeader( "Authorization" );
        if ( bearerToken != null && bearerToken.startsWith( "Bearer " ) ) {
            return bearerToken.substring( 7 );
        }
        return null;
    }
}
