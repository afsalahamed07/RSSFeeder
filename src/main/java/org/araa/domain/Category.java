package org.araa.domain;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@NoArgsConstructor
@AllArgsConstructor
@Data
@Entity
public class Category {
    @Id
    @GeneratedValue( strategy = GenerationType.IDENTITY )
    @Column( name = "category_id" )
    private Long id;

    @Column( name = "name", nullable = false, unique = true )
    private String name;

    public Category( String name ) {
        this.name = name;
    }

}
