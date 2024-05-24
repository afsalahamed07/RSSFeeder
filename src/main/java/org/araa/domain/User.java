package org.araa.domain;

import jakarta.persistence.*;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.*;


@Data
@NoArgsConstructor
@Entity
@Table( name = "users" )
public class User {

    @Id
    @GeneratedValue( strategy = GenerationType.UUID )
    @Column( name = "user_id" )
    private UUID userId;

    @Column( name = "username", nullable = false )
    private String username;

    @ManyToMany( fetch = FetchType.EAGER )
    @JoinTable( name = "user_subscriptions", joinColumns = @JoinColumn( name = "user_id", referencedColumnName = "user_id" ),
            inverseJoinColumns = @JoinColumn( name = "rss_id", referencedColumnName = "rss_id" ) )
    private Set<RSS> subscriptions;

    @Column( name = "name", nullable = false )
    private String name;

    @Column( name = "email", nullable = false )
    private String email;

    @Column( name = "password", nullable = false )
    private String password;

    @ManyToMany( fetch = FetchType.EAGER, cascade = CascadeType.MERGE )
    @JoinTable( name = "user_roles", joinColumns = @JoinColumn( name = "user_id", referencedColumnName = "user_id" ),
            inverseJoinColumns = @JoinColumn( name = "role_id", referencedColumnName = "role_id" ) )
    private List<Role> roles;

    @ManyToMany( fetch = FetchType.LAZY, cascade = CascadeType.MERGE )
    @JoinTable( name = "user_entry", joinColumns = @JoinColumn( name = "user_id", referencedColumnName = "user_id" ),
            inverseJoinColumns = @JoinColumn( name = "entry_id", referencedColumnName = "entry_id" ) )
    @OrderBy( "publishedDate DESC" )
    private Set<Entry> entries = new HashSet<>();

    @Column( name = "created_at" )
    private Date createdDate;

    @Column( name = "updated_at" )
    private Date updatedDate;
}
