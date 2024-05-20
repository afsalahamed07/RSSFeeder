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
}
