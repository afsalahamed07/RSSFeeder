package org.araa.controllers;

import lombok.AllArgsConstructor;
import org.araa.application.dto.EntryDto;
import org.araa.domain.Entry;
import org.araa.domain.User;
import org.araa.services.AuthService;
import org.araa.services.EntryService;
import org.araa.services.UserService;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@AllArgsConstructor
@RestController
@RequestMapping( "/api/v2/entries" )
public class EntryController {
    private EntryService entryService;
    private final AuthService authService;
    private final UserService userService;

    @GetMapping()
    public Entry fetchEntry( String entryUrl ) {
        return entryService.fetchEntry( entryUrl );
    }

    @PostMapping()
    public Entry saveEntry( Entry entry ) {
        return entryService.saveEntry( entry );
    }


    @GetMapping( "all_entries" )
    public ResponseEntity<List<EntryDto>> fetchEntries( @RequestParam int page, @RequestParam int size ) {
        UserDetails userDetails = authService.getAuthenticatedUser();
        User user = userService.getUserByUsername( userDetails.getUsername() );
        List<Entry> entries = entryService.fetchEntries( user, page, size );
        List<EntryDto> entryDtos = entries.stream().map( EntryDto::new ).toList();
        return ResponseEntity.ok( entryDtos );
    }
}
