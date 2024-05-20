package org.araa.services;

import com.rometools.rome.feed.synd.SyndFeed;
import com.rometools.rome.io.FeedException;
import com.rometools.rome.io.ParsingFeedException;
import lombok.AllArgsConstructor;
import lombok.NonNull;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.araa.application.dto.RSSDto;
import org.araa.domain.RSS;
import org.araa.infrastructure.utility.XMLParser;
import org.araa.repositories.RSSRepository;
import org.hibernate.FetchNotFoundException;
import org.springframework.stereotype.Service;

import java.util.Date;
import java.util.List;

@AllArgsConstructor
@Service
public class RSSService {

    private static final Logger logger = LogManager.getLogger( RSSService.class );

    private final RSSRepository rssRepository;


    public RSS registerRSS( String url ) throws FeedException {
        RSS rss;
        if ( rssRepository.existsByUrl( url ) ) {
            logger.info( "RSS already exists for {}", url );
            rss = rssRepository.findByUrl( url );
        } else {
            try {
                SyndFeed syndFeed = XMLParser.parse( url );
                rss = createRSSfromSyndFeed( syndFeed, url );
                rss = rssRepository.save( rss );
                logger.info( "RSS created for {}", url );
            } catch ( FeedException e ) {
                throw new ParsingFeedException( "Failed to parse and create feed", e );
            }
        }
        return rss;
    }

    private RSS createRSSfromSyndFeed( @NonNull SyndFeed syndFeed, String url ) {
        RSS rss = new RSS();
        rss.setUrl( url );
        rss.setFeedType( syndFeed.getFeedType() );
        rss.setDescription( syndFeed.getDescription() );
        rss.setTitle( syndFeed.getTitle() );
        rss.setCreatedDate( new Date() );
        return rss;
    }

    public RSS getRSS( String url ) throws FetchNotFoundException {
        if ( rssRepository.existsByUrl( url ) )
            return rssRepository.findByUrl( url );

        throw new FetchNotFoundException( "RSS", url );
    }

    public List<RSSDto> getAllRSS() {
        List<RSS> rssList = rssRepository.findAll();
        return RSSDto.from( rssList );
    }
}
