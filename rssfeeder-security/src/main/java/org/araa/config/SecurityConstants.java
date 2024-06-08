package org.araa.config;

public class SecurityConstants {

    private SecurityConstants() {
        throw new IllegalStateException( "Utility class" );
    }

    public static final long JWT_EXPIRATION_TIME = 100_000_000; // 1 day
    public static final String JWT_SECRET = "secret_big_enough_for_production_security_1234567890_hopefully";
}
