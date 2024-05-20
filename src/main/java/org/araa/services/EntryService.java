package org.araa.services;

import lombok.AllArgsConstructor;
import org.araa.domain.Entry;
import org.araa.domain.User;
import org.araa.repositories.EntryRepository;
import org.hibernate.FetchNotFoundException;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.util.List;

@AllArgsConstructor
@Service
public class EntryService {

    private EntryRepository entryRepository;

    public Entry fetchEntry( String entryLink ) {
        if ( entryRepository.existsByLink( entryLink ) )
            return entryRepository.findByLink( entryLink );
        throw new FetchNotFoundException( "Entry", entryLink );
    }


    public Entry saveEntry( Entry entry ) {
        if ( entryRepository.existsByLink( entry.getLink() ) )
            return entryRepository.findByLink( entry.getLink() );
        return entryRepository.save( entry );
    }

    public List<Entry> fetchEntries( User user, int page, int size ) {
        Pageable pageable = PageRequest.of( page, size );
        Page<Entry> entries = entryRepository.findEntriesByUserId( user.getUserId(), pageable );
        return entries.getContent();
    }
}
