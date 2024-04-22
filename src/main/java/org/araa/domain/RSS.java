package org.araa.domain;

import jakarta.persistence.*;
import lombok.Data;

import java.util.Set;

@Entity
@Data
@Table(name = "rss")
public class RSS {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long rssId;

    private String url;

}
