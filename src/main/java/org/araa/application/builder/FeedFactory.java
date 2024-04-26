package org.araa.application.builder;

import com.rometools.rome.feed.synd.SyndFeed;
import lombok.AllArgsConstructor;
import lombok.NonNull;
import org.araa.application.dto.Feed;
import org.springframework.stereotype.Component;

@Component
@AllArgsConstructor
public class FeedFactory {
    private final EntryBuilder entryBuilder;

    public Feed createFeed( @NonNull SyndFeed syndFeed ) {
        Feed feed = new Feed();
        feed.setTitle( syndFeed.getTitle() );
        feed.setDescription( syndFeed.getDescription() );
        feed.setFeedType( syndFeed.getFeedType() );
        feed.setLink( syndFeed.getLink() );
        feed.setEntries( syndFeed.getEntries().stream().map( entryBuilder::buildEntry ).toList() );
        return feed;
    }
}
