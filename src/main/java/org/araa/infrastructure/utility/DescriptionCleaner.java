package org.araa.infrastructure.utility;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.jdom2.output.Format;
import org.jdom2.output.XMLOutputter;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;
import org.springframework.stereotype.Component;

@Component
public class DescriptionCleaner {
    final static Logger logger = LogManager.getLogger(DescriptionCleaner.class);

    public String cleanDescriptionWithHTML(String htmlContent) {
        try {
            // Parse the HTML content
            Document doc = Jsoup.parse(htmlContent);

            // Remove all <img> elements from the document
            Elements imgs = doc.select("img");
            for (Element img : imgs) {
                img.remove();
            }

            // Remove any other unwanted elements as needed, e.g., <script>, <style>
            doc.select("script, style").remove();

            // Return the cleaned HTML as a string
            return doc.body().html(); // Using .html() to get the inner HTML of the <body> tag
        } catch (Exception e) {
            logger.error("Error cleaning description", e);
            return null; // Return null or alternatively an empty string if that's preferable
        }
    }

    public String cleanDescription(String htmlContent) {
        try {
            // Parse the HTML content
            Document doc = Jsoup.parse(htmlContent);

            // Remove all <img> elements from the document
            Elements imgs = doc.select("img");
            for (Element img : imgs) {
                img.remove();
            }

            // Remove any other unwanted elements as needed, e.g., <script>, <style>
            doc.select("script, style").remove();

            // Return the plain text of the body, without any HTML tags
            return doc.body().text(); // Using .text() to get only the text content, stripping all HTML tags
        } catch (Exception e) {
            logger.error("Error cleaning description", e);
            return null; // Return null or alternatively an empty string if that's preferable
        }
    }

}

