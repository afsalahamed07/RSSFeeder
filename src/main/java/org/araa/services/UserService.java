package org.araa.services;

import lombok.AllArgsConstructor;
import org.araa.application.dto.UserRegistrationDto;
import org.araa.application.error.UserAlreadyExistError;
import org.araa.domain.Role;
import org.araa.domain.User;
import org.araa.repositories.RoleRepository;
import org.araa.repositories.UserRepository;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.Collection;
import java.util.Collections;
import java.util.List;
import java.util.stream.Collectors;

@AllArgsConstructor
@Service
public class UserService implements UserDetailsService {

    private final UserRepository userRepository;
    private final RoleRepository roleRepository;
    private PasswordEncoder passwordEncoder;

    // Save a new profile
    public User registerUser(User user) {
        return userRepository.save(user);
    }

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

    public User registerUser(String username) {
        User user = new User();
        user.setUsername(username);
        return userRepository.save(user);
    }

    // Retrieve all profiles
    public List<User> getAllProfiles() {
        return userRepository.findAll();
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
}
