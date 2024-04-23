package org.araa.services;

import lombok.AllArgsConstructor;
import org.araa.application.dto.AuthResponseDTO;
import org.araa.application.dto.LoginCredentialsDto;
import org.araa.application.security.JWTGenerator;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;

@AllArgsConstructor
@Service
public class AuthService {

    private final AuthenticationManager authenticationManager;
    private JWTGenerator jwtGenerator;

    public AuthResponseDTO authenticate(LoginCredentialsDto loginCredentialsDto) {
        Authentication authentication = authenticationManager.authenticate(
                new UsernamePasswordAuthenticationToken(loginCredentialsDto.getUsername(),
                        loginCredentialsDto.getPassword())
        );
        SecurityContextHolder.getContext().setAuthentication(authentication);

        String token = jwtGenerator.generateToken(authentication);

        return new AuthResponseDTO(token);
    }
}
