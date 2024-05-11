package org.araa.infrastructure.utility;

import com.rometools.rome.feed.synd.SyndCategory;
import lombok.AllArgsConstructor;
import lombok.NonNull;
import org.araa.domain.Category;
import org.araa.services.CategoryService;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

@AllArgsConstructor
@Component
public class CategoryExtractor {
    private CategoryService categoryService;

    public Set<Category> extract( @NonNull List<SyndCategory> syndCategories ) {
        return syndCategories.stream().map( SyndCategory::getName ).map( name -> {
            Category category = new Category();
            category.setName( name );
            return categoryService.saveCategory( category );
        } ).collect( Collectors.toSet() );
    }
}
