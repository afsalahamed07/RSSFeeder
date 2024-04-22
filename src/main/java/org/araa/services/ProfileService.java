package org.araa.services;

import lombok.AllArgsConstructor;
import org.araa.application.dto.UserRegistrationDto;
import org.araa.domain.User;
import org.araa.repositories.UserRepository;
import org.springframework.stereotype.Service;

import java.util.List;

@AllArgsConstructor
@Service
public class ProfileService {

    private final UserRepository userRepository;

    // Save a new profile
    public User saveProfile(User user) {
        return userRepository.save(user);
    }

    public User saveProfile(UserRegistrationDto userRegistrationDto) {
        User user = new User();
        user.setUsername(userRegistrationDto.getUsername());
        user.setName(userRegistrationDto.getName());
        user.setEmail(userRegistrationDto.getEmail());
        user.setPassword(userRegistrationDto.getPassword());
        return userRepository.save(user);
    }

    public User saveProfile(String username) {
        User user = new User();
        user.setUsername(username);
        return userRepository.save(user);
    }

    // Retrieve all profiles
    public List<User> getAllProfiles() {
        return userRepository.findAll();
    }

//    public void addRssFeedToProfile(String username, String rssFeed) {
//        // Fetch the profile by username
//        Profile profile = profileRepository.findById(username).orElseThrow(() -> new RuntimeException("Profile not found"));
//
//        // Add the RSS feed to subscriptions list
//        List<String> subscriptions = profile.getSubscriptions();
//        if (subscriptions == null) {
//            subscriptions = new ArrayList<>();
//            profile.setSubscriptions(subscriptions);
//        }
//        subscriptions.add(rssFeed);
//
//        // Save the updated profile
//        profileRepository.save(profile);
//    }
//
//    public List<String> getRssFeedsForProfile(String username) {
//        // Fetch the profile by username
//        Profile profile = profileRepository.findById(username).orElseThrow(() -> new RuntimeException("Profile not found"));
//        return profile.getSubscriptions();
//    }
}
