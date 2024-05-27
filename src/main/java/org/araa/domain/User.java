package org.araa.domain;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.*;


@Data
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Builder
@Table( name = "users" )
public class User {

    @Id
    @GeneratedValue( strategy = GenerationType.UUID )
    @Column( name = "user_id" )
    private UUID userId;

    @Column( name = "username", nullable = false )
    private String username;

    @ManyToMany( fetch = FetchType.LAZY )
    @JoinTable( name = "user_subscriptions", joinColumns = @JoinColumn( name = "user_id", referencedColumnName = "user_id" ),
            inverseJoinColumns = @JoinColumn( name = "rss_id", referencedColumnName = "rss_id" ) )
    private Set<RSS> subscriptions;

    @Column( name = "name", nullable = false )
    private String name;

    @Column( name = "email", nullable = false )
    private String email;

    @Column( name = "password", nullable = false )
    private String password;

    @ManyToMany( fetch = FetchType.EAGER )
    @JoinTable( name = "user_roles", joinColumns = @JoinColumn( name = "user_id", referencedColumnName = "user_id" ),
            inverseJoinColumns = @JoinColumn( name = "role_id", referencedColumnName = "role_id" ) )
    private List<Role> roles;

    @ManyToMany( fetch = FetchType.LAZY )
    @JoinTable( name = "user_entry", joinColumns = @JoinColumn( name = "user_id", referencedColumnName = "user_id" ),
            inverseJoinColumns = @JoinColumn( name = "entry_id", referencedColumnName = "entry_id" ) )
    private Set<Entry> entries;

    @Column( name = "created_at" )
    private Date createdDate;

    @Column( name = "updated_at" )
    private Date updatedDate;

    public void addEntry( Entry entry ) {
        if ( entries == null )
            entries = new HashSet<>();
        entries.add( entry );
    }
}
