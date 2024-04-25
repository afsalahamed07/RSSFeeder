package org.araa;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cache.annotation.EnableCaching;

@SpringBootApplication
@EnableCaching
public class Feeder {

    public static void main( String[] args ) {
        SpringApplication.run( Feeder.class, args );
    }

}