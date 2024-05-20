package org.araa.services;

import lombok.AllArgsConstructor;
import org.araa.application.dto.EntryDto;
import org.araa.domain.Entry;
import org.araa.domain.User;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@AllArgsConstructor
@RestController
@RequestMapping( "/api/v2/entries" )
public class EntryController {
    private final EntryService entryService;
    private final AuthService authService;
    private final UserService userService;

    @GetMapping()
    public ResponseEntity<List<EntryDto>> fetchEntries( @RequestParam int page, @RequestParam int size ) {
        UserDetails userDetails = authService.getAuthenticatedUser();
        User user = userService.getUserByUsername( userDetails.getUsername() );
        List<Entry> entries = entryService.fetchEntries( user, page, size);
        List<EntryDto> entryDtos = entries.stream().map( EntryDto::new ).toList();
        return ResponseEntity.ok( entryDtos );
    }
}
