package org.araa.services;

import lombok.AllArgsConstructor;
import lombok.NonNull;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.araa.domain.Category;
import org.araa.repositories.CategoryRepository;
import org.springframework.stereotype.Service;

import java.util.Date;

@AllArgsConstructor
@Service
public class CategoryService {
    private static final Logger logger = LogManager.getLogger( CategoryService.class );
    private CategoryRepository categoryRepository;

    public Category saveCategory( @NonNull Category category ) {
        logger.info( "Saving category: {}", category.getName() );
        if ( categoryRepository.existsByName( category.getName() ) ) {
            logger.info( "Category already exists: {}", category.getName() );
            return categoryRepository.findByName( category.getName() );
        }
        category.setCreatedDate( new Date() );
        return categoryRepository.save( category );
    }

}
