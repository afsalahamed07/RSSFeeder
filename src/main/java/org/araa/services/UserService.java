package org.araa.services;

import lombok.AllArgsConstructor;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.araa.application.dto.LoginCredentialsDto;
import org.araa.application.dto.UserRegistrationDto;
import org.araa.application.error.UserAlreadyExistError;
import org.araa.domain.Role;
import org.araa.domain.User;
import org.araa.repositories.RoleRepository;
import org.araa.repositories.UserRepository;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;

import java.util.Collection;
import java.util.Collections;
import java.util.List;
import java.util.stream.Collectors;

@AllArgsConstructor
@Service
public class UserService implements UserDetailsService {

    private static final Logger logger = LogManager.getLogger(UserService.class);
    private final UserRepository userRepository;
    private final RoleRepository roleRepository;
    private final PasswordEncoder passwordEncoder;
    private final AuthenticationManager authenticationManager;

public User registerUser(UserRegistrationDto userRegistrationDto) throws UserAlreadyExistError{
        if (userRepository.existsByUsername(userRegistrationDto.getUsername())) {
            throw new UserAlreadyExistError("Username already exists");
        }
        User user = new User();
        user.setUsername(userRegistrationDto.getUsername());
        user.setName(userRegistrationDto.getName());
        user.setEmail(userRegistrationDto.getEmail());
        user.setPassword(passwordEncoder.encode(userRegistrationDto.getPassword()));

        setUserRole(user);

        return userRepository.save(user);
    }

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        User user =  userRepository.findByUsername(username)
                .orElseThrow(() -> new UsernameNotFoundException("User not found"));

        return new org.springframework.security.core.userdetails.User(user.getUsername(), user.getPassword(), mapRolesToAuthorities(user.getRoles()));
    }

    private Collection<GrantedAuthority> mapRolesToAuthorities(List<Role> roles) {
        return roles.stream()
                .map(role -> new SimpleGrantedAuthority(role.getName()))
                .collect(Collectors.toList());
    }


    public void setUserRole(User user) {
        Role roles = roleRepository.findByName("USER").get();
        user.setRoles(Collections.singletonList(roles));
    }

    public void login(LoginCredentialsDto loginCredentialsDto) {
        logger.info("Logging in user: {}", loginCredentialsDto.getUsername());
        Authentication authentication = authenticationManager.authenticate(
                new UsernamePasswordAuthenticationToken(loginCredentialsDto.getUsername(),
                        loginCredentialsDto.getPassword())
        );
        logger.info("User logged in successfully");
        SecurityContextHolder.getContext().setAuthentication(authentication);
    }
}
