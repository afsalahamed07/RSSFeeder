package org.araa.application.dto;

import lombok.Data;
import org.araa.domain.Category;
import org.araa.domain.Entry;

import java.util.List;

@Data
public class EntryDto {
    private String title;
    private String description;
    private String link;
    private String author;
    private String thumbnail;
    private List<CategoryDto> category;

    public EntryDto( Entry entry ) {
        this.title = entry.getTitle();
        this.description = entry.getDescription();
        this.link = entry.getLink();
        this.author = entry.getAuthor();
        this.thumbnail = entry.getThumbnail();
        this.category = entry.getCategories().stream().map( CategoryDto::new ).toList();
    }

}
