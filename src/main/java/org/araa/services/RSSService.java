package org.araa.services;

import com.rometools.rome.feed.synd.SyndFeed;
import com.rometools.rome.io.FeedException;
import com.rometools.rome.io.ParsingFeedException;
import lombok.AllArgsConstructor;
import lombok.NonNull;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.araa.domain.RSS;
import org.araa.domain.User;
import org.araa.infrastructure.utility.XMLParser;
import org.araa.repositories.RSSRepository;
import org.hibernate.FetchNotFoundException;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Service;

import java.util.Date;
import java.util.Set;

@AllArgsConstructor
@Service
public class RSSService {

    private static final Logger logger = LogManager.getLogger( RSSService.class );

    private final RSSRepository rssRepository;
    private final UserService userService;


    public RSS registerRSS( String url ) throws FeedException {
        RSS rss;

        if ( SecurityContextHolder.getContext().getAuthentication() == null ) {
            logger.error( "User not authenticated" );
            throw new AuthenticationException( "User not authenticated" ) {
            };
        }

        UserDetails userDetails = ( UserDetails ) SecurityContextHolder.getContext().getAuthentication().getPrincipal();

        if ( rssRepository.existsByUrl( url ) ) {
            rss = rssRepository.findByUrl( url );
            userService.subscribeRSS( userDetails.getUsername(), rss );
        } else {
            try {
                SyndFeed syndFeed = XMLParser.parse( url );
                rss = createRSSfromSyndFeed( syndFeed, url );
                rss = rssRepository.save( rss );
                userService.subscribeRSS( userDetails.getUsername(), rss );
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
}
