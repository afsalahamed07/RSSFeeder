package org.araa.domain;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
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
    private UUID id;

    @NotNull
    @Size(
            min = 2,
            max = 255,
            message = "Name is required, maximum 255 characters."
    )
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

    public void setRole( Role role ) {
        if ( roles == null ) {
            roles = new ArrayList<>();
        }
        roles.add( role );
    }
}
