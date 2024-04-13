package org.araa.entry;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class EntryController {

    @GetMapping("/entry")
    public String getEntry() {
        return "Hello from FeedController";
    }
}
