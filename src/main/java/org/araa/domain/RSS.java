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

    @OneToMany(mappedBy = "rss", cascade = CascadeType.ALL, orphanRemoval = true)
    private Set<Subscription> subscribers;

}
