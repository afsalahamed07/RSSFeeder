package org.araa.controller;

import org.araa.service.ProfileService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class ProfileController {

    private ProfileService profileService;

    @PostMapping("/profile/{username}/addRssFeed")
    public ResponseEntity<?> addRssFeed(@PathVariable String username, @RequestBody String rssFeed) {
        profileService.addRssFeedToProfile(username, rssFeed);
        return ResponseEntity.ok().build();
    }
}
