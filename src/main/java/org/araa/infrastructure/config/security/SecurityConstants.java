package org.araa.infrastructure.config.security;

public class SecurityConstants {

    private SecurityConstants() {
        throw new IllegalStateException("Utility class");
    }

    public static final long JWT_EXPIRATION_TIME = 864_000_000; // 10 days
    public static final String JWT_SECRET = "secret";
}
