package org.araa.application.builder;

import com.rometools.rome.feed.synd.SyndEntry;
import lombok.AllArgsConstructor;
import org.araa.domain.Entry;
import org.araa.infrastructure.utility.CategoryExtractor;
import org.araa.infrastructure.utility.DescriptionCleaner;
import org.araa.infrastructure.utility.ThumbnailExtractor;
import org.springframework.stereotype.Component;

@Component
@AllArgsConstructor
public class EntryBuilder {
    private final CategoryExtractor categoryExtractor;
    private final ThumbnailExtractor thumbnailExtractor;
    private final DescriptionCleaner descriptionCleaner;

    public Entry buildEntry(SyndEntry syndEntry) {
        Entry entry = new Entry();
        entry.setTitle(syndEntry.getTitle());
        entry.setLink(syndEntry.getLink());
        entry.setPublishedDate(syndEntry.getPublishedDate());
        entry.setAuthor(syndEntry.getAuthor());
        entry.setCategories(categoryExtractor.extract(syndEntry.getCategories()));
        entry.setThumbnail(thumbnailExtractor.extractThumbnail(syndEntry));
        entry.setDescription(descriptionCleaner.cleanDescription(syndEntry.getDescription().getValue()));
//        entry.setSyndDescription(syndEntry.getDescription());
        return entry;
    }
}