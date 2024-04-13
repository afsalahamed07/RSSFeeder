package org.araa.profile;

import lombok.AllArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@AllArgsConstructor
@RestController
public class ProfileController {

    private final ProfileService profileService;

    @PostMapping("/profile")
    public ResponseEntity<Profile> createProfile(@RequestBody String username) {
        // this is I/O bound operation
        Profile profile = profileService.saveProfile(username);
        return ResponseEntity.ok(profile);
    }

    @PostMapping("/profile/{username}/addRssFeed")
    public ResponseEntity<?> addRssFeed(@PathVariable String username, @RequestBody String rssFeed) {
        profileService.addRssFeedToProfile(username, rssFeed);
        return ResponseEntity.ok().build();
    }

    @GetMapping
    public ResponseEntity<?> getRssFeeds(@PathVariable String username) {
        return ResponseEntity.ok(profileService.getRssFeedsForProfile(username));
    }
}
