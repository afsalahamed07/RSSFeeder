package org.araa.feed;

import com.rometools.rome.io.FeedException;
import org.jdom2.JDOMException;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.io.IOException;

@RestController
public class FeedController {
        @GetMapping("/feed")
        public Feed getFeed(@RequestParam("rss") String rss) {
            try {
                return new Feed(rss);
            } catch (IOException | JDOMException | FeedException e) {
                throw new RuntimeException("Error fetching feed", e);
            }
        }
}
