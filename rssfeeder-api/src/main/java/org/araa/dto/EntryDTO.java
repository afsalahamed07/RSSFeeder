package org.araa.dto;

import lombok.Data;
import org.araa.domain.Entry;

import java.util.Date;
import java.util.List;

@Data
public class EntryDTO {
    private String title;
    private String description;
    private String link;
    private String author;
    private String thumbnail;
    private List<CategoryDto> category;
    private Date publishedDate;

    public EntryDTO( Entry entry ) {
        this.title = entry.getTitle();
        this.description = entry.getDescription();
        this.link = entry.getLink();
        this.author = entry.getAuthor();
        this.thumbnail = entry.getThumbnail();
        this.category = entry.getCategories().stream().map( CategoryDto::new ).toList();
        this.publishedDate = entry.getPublishedDate();
    }

}
