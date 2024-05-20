package org.araa.application.dto;

import lombok.Data;
import org.araa.domain.Category;

@Data
public class CategoryDto {
    private String name;

    public CategoryDto( Category category ) {
        this.name = category.getName();
    }
}

