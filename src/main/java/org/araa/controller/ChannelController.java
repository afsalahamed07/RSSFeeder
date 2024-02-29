package org.araa.controller;

import com.rometools.rome.feed.synd.SyndFeed;
import com.rometools.rome.feed.synd.SyndFeedImpl;
import com.rometools.rome.io.FeedException;
import com.rometools.rome.io.SyndFeedInput;
import org.araa.entity.Feed;
import org.araa.utility.FeedFetcher;
import org.jdom2.Document;
import org.jdom2.JDOMException;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.io.IOException;
import java.nio.charset.StandardCharsets;

@RestController
public class ChannelController {
        @GetMapping("/channel")
        public Feed getChannel(@RequestParam("rss") String rss) throws IOException, JDOMException, FeedException {
            FeedFetcher feedFetcher = FeedFetcher.INSTANCE;
            // Assuming decodeURL method exists and properly decodes the URL
            String decodedUrl = java.net.URLDecoder.decode(rss, StandardCharsets.UTF_8);
            Document document = feedFetcher.parseFeed(decodedUrl);
            SyndFeedInput input = new SyndFeedInput();
            SyndFeed feed = input.build(document);
            return new Feed(feed);
        }
}
