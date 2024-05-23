package org.araa.services;

import com.rometools.rome.feed.synd.SyndEntry;
import lombok.AllArgsConstructor;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.araa.domain.Entry;
import org.araa.domain.RSS;
import org.araa.domain.User;
import org.araa.infrastructure.utility.DescriptionCleaner;
import org.araa.infrastructure.utility.ThumbnailExtractor;
import org.araa.repositories.EntryRepository;
import org.hibernate.FetchNotFoundException;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.util.Date;
import java.util.List;
import java.util.Optional;

@AllArgsConstructor
@Service
public class EntryService {

    private EntryRepository entryRepository;
    private static final Logger logger = LogManager.getLogger( EntryService.class );

    public Entry from( SyndEntry syndEntry ) {

        return Entry.builder().
                title( syndEntry.getTitle() ).
                link( syndEntry.getLink().strip() ).
                publishedDate( syndEntry.getPublishedDate() ).
                author( syndEntry.getAuthor() ).
                thumbnail( ThumbnailExtractor.extractThumbnail( syndEntry ) ).
                description( DescriptionCleaner.cleanDescription( syndEntry.getDescription().getValue() ) ).
                createdDate( new Date() ).
                build();
    }

    public Entry fetchEntry( String entryLink ) {
        if ( entryRepository.existsByLink( entryLink ) ) {
            Optional<Entry> optionalEntry = entryRepository.findByLink( entryLink );
            if ( optionalEntry.isPresent() )
                return optionalEntry.get();
        }
        throw new FetchNotFoundException( "Entry", entryLink );
    }


    public Entry saveEntry( Entry entry ) {
        if ( entryRepository.existsByLink( entry.getLink() ) ) {
            logger.info( "Entry already exists for {}", entry.getLink() );
            return fetchEntry( entry.getLink() );
        }
        logger.info( "Saving entry {}", entry );
        return entryRepository.save( entry );
    }

    public List<Entry> fetchEntries( User user, int page, int size ) {
        Pageable pageable = PageRequest.of( page, size );
        Page<Entry> entries = entryRepository.findEntriesByUserId( user.getUserId(), pageable );
        return entries.getContent();
    }

    public void processEntry( SyndEntry syndEntry, RSS rss ) {
        // todo : add categories
        Entry entry = from( syndEntry );
        entry.setRss( rss );
        try {
            entry = saveEntry( entry );
        } catch ( Exception e ) {
            logger.info( "Failed to save entry {}", entry );
            logger.info( e.getMessage() );
        }
        logger.info( "Entry saved {}", entry );
    }
}
