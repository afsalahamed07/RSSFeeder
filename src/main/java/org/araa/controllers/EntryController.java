package org.araa.controllers;

import org.araa.domain.Entry;
import org.araa.services.EntryService;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/entry")
public class EntryController {
    private EntryService entryService;

    @GetMapping("/entry")
    public Entry fetchEntry( String entryUrl) {
        return entryService.fetchEntry( entryUrl );
    }

    @PostMapping("/entry")
    public Entry saveEntry( Entry entry ) {
        return entryService.saveEntry( entry );
    }
}
