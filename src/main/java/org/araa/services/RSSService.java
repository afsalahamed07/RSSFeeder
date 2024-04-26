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
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Service;

import java.util.Date;

@AllArgsConstructor
@Service
public class RSSService {

    private static final Logger logger = LogManager.getLogger( RSSService.class );

    private final RSSRepository rssRepository;
    private final UserService userService;


    public RSSDto registerRSS( String url ) throws FeedException {

        if ( SecurityContextHolder.getContext().getAuthentication() == null ) {
            logger.error( "User not authenticated" );
            throw new AuthenticationException( "User not authenticated" ) {
            };
        }

        UserDetails userDetails = ( UserDetails ) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        RSSDto rssDto = new RSSDto();

        if ( rssRepository.existsByUrl( url ) ) {
            RSS rss = rssRepository.findByUrl( url );

            userService.subscribeRSS( userDetails.getUsername(), rss );
            rssDto.setUrl( rss.getUrl() );
            rssDto.setFeedType( rss.getFeedType() );
            rssDto.setDescription( rss.getDescription() );
            rssDto.setTitle( rss.getTitle() );
        } else {
            try {
                SyndFeed syndFeed = XMLParser.parse( url );
                RSS rss = createRSSfromSyndFeed( syndFeed , url);

                rss = rssRepository.save( rss );

                rssDto.setUrl( rss.getUrl() );
                rssDto.setFeedType( rss.getFeedType() );
                rssDto.setDescription( rss.getDescription() );
                rssDto.setTitle( rss.getTitle() );


                userService.subscribeRSS( userDetails.getUsername(), rss );

            } catch ( FeedException e ) {
                throw new ParsingFeedException( "Failed to parse and create feed", e );
            }

        }


        return rssDto;
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

}
