package org.araa.controller;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class FeedController {

    @GetMapping("/feed")
    public String getFeed() {
        return "Hello from FeedController";
    }
}
