package org.araa.utility;

import lombok.experimental.UtilityClass;
import org.jdom2.Document;
import org.jdom2.input.SAXBuilder;
import org.jsoup.Jsoup;

import java.io.StringReader;
import java.util.ArrayList;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

@UtilityClass
public class HTMLUtility {
    private static final Pattern IMG_TAG_PATTERN = Pattern.compile( "<img[^>]*>" );

    public static List<String> extractImageUrls( String htmlContent ) {
        List<String> imageUrls = new ArrayList<>();
        Matcher matcher = IMG_TAG_PATTERN.matcher( htmlContent );

        while ( matcher.find() ) {
            imageUrls.add( matcher.group( 1 ) );  // Capture the content of src attribute
        }

        return imageUrls;
    }

    public static String removeImageTags( String htmlContent ) {
        return IMG_TAG_PATTERN.matcher( htmlContent ).replaceAll( "" );
    }

    public static Document jdomHtmlParser( String htmlContent ) {
        SAXBuilder saxBuilder = new SAXBuilder();
        try {
            return saxBuilder.build( new StringReader( "<root>" + htmlContent + "</root>" ) );
        } catch ( Exception e ) {
            throw new RuntimeException( "Error parsing HTML content", e );
        }
    }

    public static org.jsoup.nodes.Document jsoupHtmlParser( String htmlContent ) {
        return Jsoup.parse( htmlContent );
    }
}
