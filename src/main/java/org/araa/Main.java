package org.araa;

import com.rometools.rome.feed.synd.SyndEntry;
import com.rometools.rome.feed.synd.SyndFeed;
import com.rometools.rome.io.SyndFeedInput;
import com.rometools.rome.io.SyndFeedOutput;
import com.rometools.rome.io.XmlReader;

import java.io.File;
import java.io.FileWriter;
import java.net.URL;

public class Main {
    public static void main(String[] args) {
        try {
            URL feedSource = new URL("https://feeds.bbci.co.uk/news/world/rss.xml");
            SyndFeedInput input = new SyndFeedInput();
            SyndFeed feed = input.build(new XmlReader(feedSource));

            for (SyndEntry entry : feed.getEntries()) {
                System.out.println(entry.getTitle());
                System.out.println(entry.getLink());
                System.out.println(entry.getComments());
                System.out.println(entry.getUri());
                System.out.println(entry.getForeignMarkup());
                System.out.println(entry.findRelatedLink("xmlns:media"));
                System.out.println("====================");
            }

            System.out.println(feed.getTitle());

//            File file = new File("feed.xml");
//            output.output(feed, new FileWriter(file));

//            System.out.println("Feed written to file successfully.");
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
