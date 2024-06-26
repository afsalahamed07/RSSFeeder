package org.araa.domain;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;
import java.util.Date;
import java.util.Set;
import java.util.UUID;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
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
    private UUID id;

    private String title;

    @Column( nullable = false )
    private String link;

    @Column( name = "published_date" )
    private Date publishedDate;

    private String author;
    private String thumbnail;

    @Column( columnDefinition = "TEXT" )
    private String description;

    @ManyToOne( fetch = FetchType.LAZY )
    private RSS rss;

    @ManyToMany( fetch = FetchType.EAGER )
    @JoinTable( name = "entry_category",
            joinColumns = @JoinColumn( name = "entry_id", referencedColumnName = "entry_id" ),
            inverseJoinColumns = @JoinColumn( name = "category_id", referencedColumnName = "category_id" ) )
    private Set<Category> categories;


    @Column( name = "created_at" )
    private Date createdDate;

    @Column( name = "updated_at" )
    private Date updatedDate;
}