package org.araa.domain;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Date;
import java.util.List;
import java.util.Set;

@Data
@Entity
public class Entry {
    /**
     * This is wrapper class for SyndEntry
     * It is used to extract the required fields from SyndEntry
     * and send it to the client
     */

    @Id
    @GeneratedValue( strategy = GenerationType.IDENTITY )
    @Column( name = "entry_id" )
    private Long id;

    private String title;

    @Column( nullable = false, unique = true )
    private String link;

    @Column( name = "published_date" )
    private Date publishedDate;

    private String author;
    private String thumbnail;
    private String description;

    @ManyToOne( fetch = FetchType.EAGER, cascade = CascadeType.ALL )
    private RSS rss;

    @ManyToMany( fetch = FetchType.EAGER, cascade = CascadeType.ALL )
    @JoinTable( name = "entry_category",
            joinColumns = @JoinColumn( name = "entry_id", referencedColumnName = "entry_id" ),
            inverseJoinColumns = @JoinColumn( name = "category_id", referencedColumnName = "category_id" ) )
    private Set<Category> categories;


    public void addCategory( Category category ) {
        categories.add( category );
    }
}