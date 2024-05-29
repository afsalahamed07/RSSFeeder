package org.araa.domain;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;
import java.util.Date;
import java.util.UUID;

@NoArgsConstructor
@AllArgsConstructor
@Data
@Builder
@Entity
public class Category implements Serializable {
    @Id
    @GeneratedValue( strategy = GenerationType.UUID )
    @Column( name = "category_id" )
    private UUID id;

    @Column( name = "name", nullable = false, unique = true )
    private String name;

    @Column( name = "created_at" )
    private Date createdDate;

    @Column( name = "updated_at" )
    private Date updatedDate;

    public Category( String name ) {
        this.name = name;
    }

}
