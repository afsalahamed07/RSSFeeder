package org.araa.controllers;

import lombok.AllArgsConstructor;
import org.araa.application.dto.UserRegistrationDto;
import org.araa.domain.User;
import org.araa.services.UserService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@AllArgsConstructor
@RestController
public class UserController {

    private final UserService userService;

    @PostMapping("/register_profile")
    public ResponseEntity<User> registerUser(@RequestBody UserRegistrationDto userRegistrationDto) {
        // this is I/O bound operation
        User user = userService.registerUser(userRegistrationDto);
        return ResponseEntity.ok(user);
    }
}
