package org.araa.controllers;

import lombok.AllArgsConstructor;
import org.araa.application.builder.LoginCredentials;
import org.araa.application.dto.UserRegistrationDto;
import org.araa.services.AuthService;
import org.araa.services.UserService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@AllArgsConstructor
@RestController
@RequestMapping("/api/auth")
public class AuthController {
    private final AuthService authService;
    private final UserService userService;


    @PostMapping("/register")
    public ResponseEntity<String> register(@RequestBody UserRegistrationDto request) {
        userService.registerUser(request);
        return ResponseEntity.ok("User registered successfully");
    }

    @PostMapping("/login")
    public ResponseEntity<String> login(@RequestBody LoginCredentials request) {
        String token = authService.authenticate(request.getUsername(), request.getPassword());
        return ResponseEntity.ok(token);
    }
}
