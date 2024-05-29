package org.araa.services;

import com.rometools.rome.feed.synd.SyndEntry;
import com.rometools.rome.feed.synd.SyndFeed;
import jakarta.transaction.Transactional;
import lombok.AllArgsConstructor;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.araa.domain.Category;
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
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;

import java.util.*;

@AllArgsConstructor
@Service
public class EntryService {

    private final UserService userService;
    private EntryRepository entryRepository;
    private CategoryService categoryService;
    private static final Logger logger = LogManager.getLogger( EntryService.class );

    public Entry entryFrom( SyndEntry syndEntry ) {

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
        Optional<Entry> optionalEntry = entryRepository.findByLink( entryLink );
        if ( optionalEntry.isPresent() )
            return optionalEntry.get();

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
        Page<Entry> entries = entryRepository.findEntriesByUserId( user.getId(), pageable );
        return entries.getContent();
    }

    @Async
    @Transactional
    public void processEntry( SyndFeed syndFeed, RSS rss, User user ) {
        for ( SyndEntry syndEntry : syndFeed.getEntries() ) {
            Entry entry = entryFrom( syndEntry );
            entry.setRss( rss );
            try {
                Set<Category> categories = fetchCategories( syndEntry );
                entry.setCategories( categories );
                entry = saveEntry( entry );
                user.addEntry( entry );
                logger.info( "Entry {} saved", entry.getTitle() );
                userService.saveEntry( user, entry );
            } catch ( Exception e ) {
                logger.error( "Failed to process entry {} : {}", entry.getLink(), e.getMessage() );
            }
        }
    }

    public Set<Category> fetchCategories( SyndEntry entry ) {
        Set<Category> categories = new HashSet<>();
        for ( int i = 0; i < entry.getCategories().size(); i++ ) {
            Category category = Category.builder().
                    name( entry.getCategories().get( i ).getName() ).
                    createdDate( new Date() ).
                    build();
            category = categoryService.saveCategory( category );
            categories.add( category );
        }
        return categories;
    }
}
