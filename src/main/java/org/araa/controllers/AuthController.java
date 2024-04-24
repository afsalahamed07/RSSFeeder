package org.araa.controllers;

import lombok.AllArgsConstructor;
import org.araa.application.dto.AuthResponseDTO;
import org.araa.application.dto.LoginCredentialsDto;
import org.araa.application.dto.UserRegistrationDto;
import org.araa.application.dto.UserRegistrationResponseDTO;
import org.araa.application.error.UserAlreadyExistError;
import org.araa.services.AuthService;
import org.araa.services.UserService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@AllArgsConstructor
@RestController
@RequestMapping("/api/auth")
public class AuthController {
    private final UserService userService;
    private final AuthService authService;


    @PostMapping("/register")
    public ResponseEntity<UserRegistrationResponseDTO> register(@RequestBody UserRegistrationDto request) {
        try{
            UserRegistrationResponseDTO userRegistrationResponseDTO = userService.registerUser(request);
            return ResponseEntity.ok(userRegistrationResponseDTO);
        } catch (UserAlreadyExistError e) {
            return ResponseEntity.badRequest().build();
        }
    }

    @PostMapping("/login")
    public ResponseEntity<AuthResponseDTO> login(@RequestBody LoginCredentialsDto request) {
        AuthResponseDTO authResponseDTO =  authService.authenticate(request);
        return ResponseEntity.ok(authResponseDTO);
    }
}
