package org.araa.controllers;

import lombok.AllArgsConstructor;
import org.araa.domain.User;
import org.araa.dto.AuthResponseDTO;
import org.araa.dto.LoginCredentialsDto;
import org.araa.dto.UserRegistrationDto;
import org.araa.dto.UserRegistrationResponseDTO;
import errors.UserAlreadyExistError;
import org.araa.services.AuthService;
import org.araa.services.CustomUserDetailsService;
import org.araa.services.UserService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@AllArgsConstructor
@RestController
@RequestMapping( "/api/v2/auth" )
public class AuthController {
    private final AuthService authService;
    private final CustomUserDetailsService customUserDetailsService;


    @PostMapping( "/register" )
    public ResponseEntity<UserRegistrationResponseDTO> register( @RequestBody UserRegistrationDto request ) {
        try {
            User user = customUserDetailsService.registerUser(
                    request.getUsername(),
                    request.getName(),
                    request.getEmail(),
                    request.getPassword() );

            UserRegistrationResponseDTO userRegistrationResponseDTO = UserRegistrationResponseDTO.builder()
                    .username( user.getUsername() )
                    .name( user.getName() )
                    .email( user.getEmail() )
                    .build();

            return ResponseEntity.ok( userRegistrationResponseDTO );
        } catch ( UserAlreadyExistError e ) {
            return ResponseEntity.badRequest().build();
        }
    }

    @PostMapping( "/login" )
    public ResponseEntity<AuthResponseDTO> login( @RequestBody LoginCredentialsDto request ) {
        String token = authService.authenticate( request.getUsername(), request.getPassword() );
        AuthResponseDTO authResponseDTO = new AuthResponseDTO( token );
        return ResponseEntity.ok( authResponseDTO );
    }
}
