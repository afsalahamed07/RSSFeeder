package org.araa.domain;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;
import java.util.Date;
import java.util.UUID;

@Entity
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
@Table( name = "rss" )
public class RSS implements Serializable {
    @Id
    @GeneratedValue( strategy = GenerationType.UUID )
    @Column( name = "rss_id" )
    private UUID id;

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
