package org.araa.utility;

import com.rometools.rome.feed.synd.SyndContent;
import lombok.experimental.UtilityClass;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

@UtilityClass
public class DescriptionCleaner {
    private static final Logger logger = LogManager.getLogger( DescriptionCleaner.class );

    public String cleanDescriptionWithHTML( String htmlContent ) {
        try {
            // Parse the HTML content
            Document doc = Jsoup.parse( htmlContent );

            // Remove all <img> elements from the document
            Elements image = doc.select( "img" );
            for ( Element img : image ) {
                img.remove();
            }

            // Remove any other unwanted elements as needed, e.g., <script>, <style>
            doc.select( "script, style" ).remove();

            // Return the cleaned HTML as a string
            return doc.body().html(); // Using .html() to get the inner HTML of the <body> tag
        } catch ( Exception e ) {
            logger.error( "Error cleaning description", e );
            return null; // Return null or alternatively an empty string if that's preferable
        }
    }

    public String cleanDescription( SyndContent htmlContent ) {
        try {
            if ( htmlContent == null ) return "No Description Available"; // Return null if the input is null
            // Parse the HTML content
            Document doc = Jsoup.parse( htmlContent.getValue() );

            // Remove all <img> elements from the document
            Elements images = doc.select( "img" );
            for ( Element img : images ) {
                img.remove();
            }

            // Remove any other unwanted elements as needed, e.g., <script>, <style>
            doc.select( "script, style" ).remove();

            // Return the plain text of the body, without any HTML tags
            return doc.body().text(); // Using .text() to get only the text content, stripping all HTML tags
        } catch ( Exception e ) {
            logger.error( "Error cleaning description", e );
            return null; // Return null or alternatively an empty string if that's preferable
        }
    }

}

