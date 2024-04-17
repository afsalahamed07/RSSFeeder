package org.araa.services;

import lombok.AllArgsConstructor;
import org.araa.domain.Profile;
import org.araa.repositories.ProfileRepository;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@AllArgsConstructor
@Service
public class ProfileService {

    private final ProfileRepository profileRepository;

    // Save a new profile
    public Profile saveProfile(Profile profile) {
        return profileRepository.save(profile);
    }

    public Profile saveProfile(String username) {
        Profile profile = new Profile();
        profile.setUsername(username);
        return profileRepository.save(profile);
    }

    // Retrieve all profiles
    public List<Profile> getAllProfiles() {
        return profileRepository.findAll();
    }

    public void addRssFeedToProfile(String username, String rssFeed) {
        // Fetch the profile by username
        Profile profile = profileRepository.findById(username).orElseThrow(() -> new RuntimeException("Profile not found"));

        // Add the RSS feed to subscriptions list
        List<String> subscriptions = profile.getSubscriptions();
        if (subscriptions == null) {
            subscriptions = new ArrayList<>();
            profile.setSubscriptions(subscriptions);
        }
        subscriptions.add(rssFeed);

        // Save the updated profile
        profileRepository.save(profile);
    }

    public List<String> getRssFeedsForProfile(String username) {
        // Fetch the profile by username
        Profile profile = profileRepository.findById(username).orElseThrow(() -> new RuntimeException("Profile not found"));
        return profile.getSubscriptions();
    }
}
