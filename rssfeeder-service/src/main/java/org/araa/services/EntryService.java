package org.araa.services;

import com.rometools.rome.feed.synd.SyndEntry;
import com.rometools.rome.feed.synd.SyndFeed;
import jakarta.persistence.EntityManager;
import jakarta.persistence.EntityManagerFactory;
import lombok.AllArgsConstructor;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.araa.domain.Category;
import org.araa.domain.Entry;
import org.araa.domain.RSS;
import org.araa.domain.User;
import org.araa.repositories.EntryRepository;
import org.araa.utility.DescriptionCleaner;
import org.araa.utility.ThumbnailExtractor;
import org.hibernate.FetchNotFoundException;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;

import java.util.*;
import java.util.concurrent.CompletableFuture;

@AllArgsConstructor
@Service
public class EntryService {

    private EntryRepository entryRepository;
    private CategoryService categoryService;
    private EntityManagerFactory entityManagerFactory;

    private static final Logger logger = LogManager.getLogger( EntryService.class );

    public Entry entryFrom( SyndEntry syndEntry ) {
        return Entry.builder().
                title( syndEntry.getTitle() ).
                link( syndEntry.getLink().strip() ).
                publishedDate( syndEntry.getPublishedDate() ).
                author( syndEntry.getAuthor() ).
                thumbnail( ThumbnailExtractor.extractThumbnail( syndEntry ) ).
                description( DescriptionCleaner.cleanDescription( syndEntry.getDescription() ) ).
                createdDate( new Date() ).
                build();
    }

    public Entry fetchEntry( String entryLink ) {
        Optional<Entry> optionalEntry = entryRepository.findByLink( entryLink );
        if ( optionalEntry.isPresent() ) return optionalEntry.get();

        throw new FetchNotFoundException( "Entry", entryLink );
    }

    public List<Entry> fetchEntries( User user, int page, int size ) {
        Pageable pageable = PageRequest.of( page, size );
        Page<Entry> entries = entryRepository.findEntriesByUserId( user.getId(), pageable );
        return entries.getContent();
    }

    @Async
    public CompletableFuture<List<Entry>> processEntry( SyndFeed syndFeed, RSS rss ) {
        List<Entry> entries = new ArrayList<>();
        try ( EntityManager entityManager = entityManagerFactory.createEntityManager() ) {
            entityManager.getTransaction().begin();

            for ( SyndEntry syndEntry : syndFeed.getEntries() ) {
                if ( entryRepository.existsByLink( syndEntry.getLink() ) ) {
                    logger.info( "Entry already exists for {}", syndEntry.getLink() );
                    continue;
                }

                Entry entry = entryFrom( syndEntry );
                entry.setRss( rss );

                // todo: transfer to category service
                Set<Category> categories = fetchCategories( syndEntry );
                entry.setCategories( categories );

                entityManager.persist( entry );

                entries.add( entry );
                logger.info( "Entry {} saved", entry.getTitle() );
            }

            entityManager.getTransaction().commit();
        } catch ( Exception e ) {
            logger.error( "Error processing entries", e );
        }
        return CompletableFuture.completedFuture( entries );
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

    public int entries( UUID userId ) {
        return entryRepository.countByUserId( userId );
    }

    public int totalPages( int size, long totalElements ) {
        return ( int ) Math.ceil( ( double ) totalElements / size );
    }

    public EntriesDTO entriesDTO( int page, int size, User user ) {
        List<Entry> entries = fetchEntries( user, page, size );

        return EntriesDTO.builder() .entries( entryDTOs )
                .page( page )
                .size( size )
                .totalPages( totalPages( size, entries( user.getId() ) ) )
                .totalElements( entries( user.getId() ) )
                .nextPage( page + 1 )
                .previousPage( page - 1 )
                .build();
    }
}
