package org.araa.domain;

import jakarta.persistence.*;
import lombok.Data;

import java.io.Serializable;
import java.util.Date;
import java.util.HashSet;
import java.util.Set;

@Data
@Entity
public class Entry implements Serializable {
    /**
     * This is wrapper class for SyndEntry
     * It is used to extract the required fields from SyndEntry
     * and send it to the client
     */

    @Id
    @GeneratedValue( strategy = GenerationType.UUID )
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

    @ManyToOne( fetch = FetchType.LAZY, cascade = CascadeType.ALL )
    private RSS rss;

    @ManyToMany( fetch = FetchType.EAGER, cascade = CascadeType.ALL )
    @JoinTable( name = "entry_category",
            joinColumns = @JoinColumn( name = "entry_id", referencedColumnName = "entry_id" ),
            inverseJoinColumns = @JoinColumn( name = "category_id", referencedColumnName = "category_id" ) )
    private Set<Category> categories;


    @Column( name = "created_at" )
    private Date createdDate;

    @Column( name = "updated_at" )
    private Date updatedDate;

    public void addCategory( Category category ) {
        if ( categories == null ) {
            categories = new HashSet<>();
        }
        categories.add( category );
    }

}