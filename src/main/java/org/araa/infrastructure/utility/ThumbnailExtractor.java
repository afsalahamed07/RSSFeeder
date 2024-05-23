package org.araa.infrastructure.utility;

import com.rometools.rome.feed.synd.SyndEntry;
import lombok.experimental.UtilityClass;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.jdom2.Document;
import org.jdom2.Element;

@UtilityClass
public class ThumbnailExtractor {

    static final Logger logger = LogManager.getLogger( ThumbnailExtractor.class );

    public String extractThumbnail( SyndEntry entry ) {
        for ( Element element : entry.getForeignMarkup() ) {
            if ( "thumbnail".equals( element.getName() ) ) {
                return element.getAttributeValue( "url" );
            }
        }
        try {
            Document descriptionDocument = HTMLUtility.jdomHtmlParser( entry.getDescription().getValue() );
            return descriptionDocument.getRootElement().getChild( "p" ).getChild( "img" ).getAttributeValue( "src" );
        } catch ( Exception e ) {
            logger.error( "Error extracting thumbnail", e );
            return null;
        }
    }
}
