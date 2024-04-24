package org.araa.domain;

import jakarta.persistence.*;
import lombok.Data;

import java.util.Date;

@Entity
@Data
@Table(name = "rss")
public class RSS {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "rss_id")
    private Long rssId;

    @Column(name = "url", nullable = false)
    private String url;

    @Column(name = "title", nullable = false)
    private String title;

    @Column(name = "created_at")
    private Date createdDate;

    @Column(name = "updated_at")
    private Date updatedDate;

}
