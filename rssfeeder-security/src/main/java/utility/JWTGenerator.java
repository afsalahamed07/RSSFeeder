package utility;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.security.Keys;
import org.araa.infrastructure.config.security.SecurityConstants;
import org.springframework.security.authentication.AuthenticationCredentialsNotFoundException;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Component;

import javax.crypto.SecretKey;
import java.nio.charset.StandardCharsets;
import java.util.Date;

import static org.araa.infrastructure.config.security.SecurityConstants.JWT_SECRET;

@Component
public class JWTGenerator {

    private final SecretKey key = Keys.hmacShaKeyFor( JWT_SECRET.getBytes( StandardCharsets.UTF_8 ) );

    public String generateToken( Authentication authentication ) {
        String username = authentication.getName();
        Date currentDate = new Date();
        Date expirationDate = new Date( currentDate.getTime() + SecurityConstants.JWT_EXPIRATION_TIME );


        return Jwts.builder()
                .subject( username )
                .issuedAt( currentDate )
                .expiration( expirationDate )
                .signWith( key )
                .compact();
    }

    public String getUsernameFromJWT( String token ) {
        Claims claims = Jwts.parser()
                .verifyWith( key )
                .build()
                .parseSignedClaims( token )
                .getPayload();
        return claims.getSubject();
    }

    public boolean validateToken( String token ) {
        try {
            Jwts.parser().verifyWith( key ).build().parseSignedClaims( token );
            return true;
        } catch ( Exception e ) {
            throw new AuthenticationCredentialsNotFoundException( "JWT was expired or invalid." );
        }
    }
}
