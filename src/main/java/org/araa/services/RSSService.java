package org.araa.services;

import com.rometools.rome.feed.synd.SyndFeed;
import lombok.AllArgsConstructor;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.araa.domain.RSS;
import org.araa.repositories.RSSRepository;
import org.hibernate.FetchNotFoundException;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;

import java.util.Date;
import java.util.List;

@AllArgsConstructor
@Service
public class RSSService {

    private static final Logger logger = LogManager.getLogger( RSSService.class );

    private final RSSRepository rssRepository;

    public RSS registerRSS( SyndFeed syndFeed ) {
        RSS rss = from( syndFeed );
        return save( rss );
    }

    public RSS save( RSS rss ) {
        if ( rssRepository.existsByUrl( rss.getUrl() ) ) {
            logger.info( "RSS already exists for {}", rss.getUrl() );
            rss = rssRepository.findByUrl( rss.getUrl() );
        } else {
            rss = rssRepository.save( rss );
            logger.info( "RSS created for {}", rss.getUrl() );
        }
        return rss;
    }

    public RSS getRSS( String url ) throws FetchNotFoundException {
        if ( rssRepository.existsByUrl( url ) )
            return rssRepository.findByUrl( url );

        throw new FetchNotFoundException( "RSS", url );
    }


    public List<RSS> getAllRSS() {
        return rssRepository.findAll();
    }

    public RSS from( SyndFeed syndFeed ) {
        return RSS.builder().
                url( syndFeed.getUri() ).
                feedType( syndFeed.getFeedType() ).
                description( syndFeed.getDescription() ).
                title( syndFeed.getTitle() ).
                createdDate( new Date() ).
                updatedDate( new Date() ).
                build();
    }

    @Async
    public void updateRSS( RSS rss ) {
        rss.setUpdatedDate( new Date() );
        rssRepository.save( rss );
        logger.info( "RSS updated for {}", rss.getUrl() );
    }
}
