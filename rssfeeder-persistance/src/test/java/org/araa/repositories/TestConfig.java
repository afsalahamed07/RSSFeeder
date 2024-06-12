package org.araa.repositories;

import org.springframework.boot.autoconfigure.domain.EntityScan;
import org.springframework.boot.test.context.TestConfiguration;
import org.springframework.context.annotation.Bean;
import org.springframework.beans.factory.annotation.Autowired;

@TestConfiguration
@EntityScan("org.araa.domain")
public class TestConfig {

    @Autowired
    private UserRepository userRepository;

    @Bean
    public UserRepository userRepository() {
        return userRepository;
    }
}