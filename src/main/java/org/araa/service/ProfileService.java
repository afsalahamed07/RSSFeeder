package org.araa.service;

import org.araa.entity.Profile;
import org.araa.repository.ProfileRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service
public class ProfileService {

    private ProfileRepository profileRepository;

    // Save a new profile
    public Profile saveProfile(Profile profile) {
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
}
