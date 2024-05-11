package org.araa.services;

import lombok.AllArgsConstructor;
import org.araa.domain.Entry;
import org.araa.repositories.EntryRepository;
import org.hibernate.FetchNotFoundException;
import org.springframework.stereotype.Service;

@AllArgsConstructor
@Service
public class EntryService {

    private EntryRepository entryRepository;

    public Entry fetchEntry( String entryUrl) {
        if ( entryRepository.existsByUrl( entryUrl ) )
            return entryRepository.findByUrl( entryUrl );
        throw new FetchNotFoundException( "Entry", entryUrl );
    }


    public Entry saveEntry( Entry entry ) {
        if ( entryRepository.existsByUrl( entry.getUrl() ) )
            return entryRepository.findByUrl( entry.getUrl() );
        return entryRepository.save( entry );
    }
}
