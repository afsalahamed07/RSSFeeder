package org.araa.domain;

import jakarta.persistence.*;
import lombok.Data;

import java.io.Serializable;
import java.util.Date;

@Entity
@Data
@Table( name = "rss" )
public class RSS implements Serializable {

    @Id
    @GeneratedValue( strategy = GenerationType.UUID )
    @Column( name = "rss_id" )
    private Long rssId;

    @Column( name = "url", nullable = false, unique = true )
    private String url;

    @Column( name = "title", nullable = false )
    private String title;

    @Column( name = "description", nullable = false )
    private String description;

    @Column( name = "feed_type", nullable = false )
    private String feedType;

    @Column( name = "created_at" )
    private Date createdDate;

    @Column( name = "updated_at" )
    private Date updatedDate;

}
