package org.araa.infrastructure.utility;

import com.rometools.rome.feed.synd.SyndCategory;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.stream.Collectors;

@Component
public class CategoryExtractor {
    public List<String> extract(List<SyndCategory> syndCategories) {
        return syndCategories.stream().map(SyndCategory::getName).collect(Collectors.toList());
    }
}
