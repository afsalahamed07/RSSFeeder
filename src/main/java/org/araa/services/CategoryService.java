package org.araa.services;

import lombok.AllArgsConstructor;
import lombok.NonNull;
import org.araa.domain.Category;
import org.araa.repositories.CategoryRepository;
import org.springframework.stereotype.Service;

import java.util.Date;

@AllArgsConstructor
@Service
public class CategoryService {

    private CategoryRepository categoryRepository;

    public Category saveCategory( @NonNull Category category ) {
        if ( categoryRepository.existsByName( category.getName() ) )
            return categoryRepository.findByName( category.getName() );
        category.setCreatedDate( new Date() );
        return categoryRepository.save( category );
    }

}
