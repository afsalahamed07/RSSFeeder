package org.araa.infrastructure.utility;

import com.rometools.rome.feed.synd.SyndCategory;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
public class CategoryExtractor {
    public List<String> extract( List<SyndCategory> syndCategories ) {
        return syndCategories.stream().map( SyndCategory::getName ).toList();
    }
}
