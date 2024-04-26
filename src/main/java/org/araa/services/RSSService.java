package org.araa.services;

import com.rometools.rome.feed.synd.SyndFeed;
import lombok.AllArgsConstructor;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.araa.application.dto.RSSDto;
import org.araa.domain.RSS;
import org.araa.infrastructure.utility.XMLParser;
import org.araa.repositories.RSSRepository;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.web.context.HttpSessionSecurityContextRepository;
import org.springframework.stereotype.Service;

import java.util.Date;

@AllArgsConstructor
@Service
public class RSSService {

    private static final Logger logger = LogManager.getLogger( RSSService.class );

    private final RSSRepository rssRepository;


    public RSSDto registerRSS( String url ) {

        if ( SecurityContextHolder.getContext().getAuthentication() == null ) {
            logger.error( "User not authenticated" );
            throw new RuntimeException( "User not authenticated" );
        }

        UserDetails userDetails = (UserDetails) SecurityContextHolder.getContext().getAuthentication().getPrincipal();


        try {
            SyndFeed syndFeed = XMLParser.parse( url );
            RSS rss = new RSS();
            rss.setUrl( url );
            rss.setFeedType( syndFeed.getFeedType() );
            rss.setDescription( syndFeed.getDescription() );
            rss.setTitle( syndFeed.getTitle() );
            rss.setCreatedDate( new Date() );
            rss = rssRepository.save( rss );

            RSSDto rssDto = new RSSDto();
            rssDto.setUrl( rss.getUrl() );
            rssDto.setFeedType( rss.getFeedType() );
            rssDto.setDescription( rss.getDescription() );
            rssDto.setTitle( rss.getTitle() );

            return rssDto;
        } catch ( Exception e ) {
            throw new RuntimeException( "Failed to parse and create feed", e );
        }
    }
}
