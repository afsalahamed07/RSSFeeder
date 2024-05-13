package org.araa.domain;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;
import java.util.Date;

@NoArgsConstructor
@AllArgsConstructor
@Data
@Entity
public class Category implements Serializable {
    @Id
    @GeneratedValue( strategy = GenerationType.IDENTITY )
    @Column( name = "category_id" )
    private Long id;

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
