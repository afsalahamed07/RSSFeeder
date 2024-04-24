package org.araa.services;

import lombok.AllArgsConstructor;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.araa.application.dto.UserRegistrationDto;
import org.araa.application.dto.UserRegistrationResponseDTO;
import org.araa.application.error.UserAlreadyExistError;
import org.araa.domain.Role;
import org.araa.domain.User;
import org.araa.repositories.RoleRepository;
import org.araa.repositories.UserRepository;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.Collections;
import java.util.Date;
import java.util.List;

@AllArgsConstructor
@Service
public class UserService implements UserDetailsService {

    private static final Logger logger = LogManager.getLogger(UserService.class);
    private final UserRepository userRepository;
    private final RoleRepository roleRepository;
    private final PasswordEncoder passwordEncoder;

public UserRegistrationResponseDTO registerUser(UserRegistrationDto userRegistrationDto) throws UserAlreadyExistError{
        if (Boolean.TRUE.equals(userRepository.existsByUsername(userRegistrationDto.getUsername()))) {
            throw new UserAlreadyExistError("Username already exists");
        }
        User user = new User();
        user.setUsername(userRegistrationDto.getUsername());
        user.setName(userRegistrationDto.getName());
        user.setEmail(userRegistrationDto.getEmail());
        user.setPassword(passwordEncoder.encode(userRegistrationDto.getPassword()));
        user.setCreatedDate(new Date());

        setUserRole(user);

        user = userRepository.save(user);

        UserRegistrationResponseDTO userRegistrationResponseDTO = new UserRegistrationResponseDTO();
        userRegistrationResponseDTO.setUsername(user.getUsername());
        userRegistrationResponseDTO.setName(user.getName());
        userRegistrationResponseDTO.setEmail(user.getEmail());

        return userRegistrationResponseDTO;
    }

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        User user =  userRepository.findByUsername(username)
                .orElseThrow(() -> new UsernameNotFoundException("User not found"));

        return new org.springframework.security.core.userdetails.User(user.getUsername(), user.getPassword(), mapRolesToAuthorities(user.getRoles()));
    }

    private List<SimpleGrantedAuthority> mapRolesToAuthorities(List<Role> roles) {
        return roles.stream()
                .map(role -> new SimpleGrantedAuthority(role.getType()))
                .toList();
    }


    public void setUserRole(User user) {
        Role roles = roleRepository.findByType("USER").orElseThrow(
                () -> new RuntimeException("Role not found")
        );
        user.setRoles(Collections.singletonList(roles));
    }

}
