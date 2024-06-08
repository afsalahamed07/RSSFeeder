package org.araa.config;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.web.AuthenticationEntryPoint;
import org.springframework.stereotype.Component;

import java.io.IOException;

@Component
public class JwtAuthEntryPoint implements AuthenticationEntryPoint {
    private final Logger logger = LogManager.getLogger( JwtAuthEntryPoint.class );

    @Override
    public void commence( HttpServletRequest request, HttpServletResponse response, AuthenticationException authException ) throws IOException {
        logger.warn( "Unauthorized error. Message - {}", authException.getMessage() );
        response.sendError( HttpServletResponse.SC_UNAUTHORIZED, authException.getMessage() );
    }
}
