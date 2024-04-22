package org.araa.controllers;

import lombok.AllArgsConstructor;
import org.araa.application.dto.LoginCredentialsDto;
import org.araa.application.dto.UserRegistrationDto;
import org.araa.application.error.UserAlreadyExistError;
import org.araa.services.UserService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@AllArgsConstructor
@RestController
@RequestMapping("/api/auth")
public class AuthController {
    private final UserService userService;


    @PostMapping("/register")
    public ResponseEntity<String> register(@RequestBody UserRegistrationDto request) {
        try{
            userService.registerUser(request);
            return ResponseEntity.ok("User registered successfully");
        } catch (UserAlreadyExistError e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        }
    }

    @PostMapping("/login")
    public ResponseEntity<String> login(@RequestBody LoginCredentialsDto request) {
        userService.login(request);
        return ResponseEntity.ok("User logged in successfully");
    }
}
