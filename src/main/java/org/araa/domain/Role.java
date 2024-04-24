package org.araa.domain;

import jakarta.persistence.*;
import lombok.Data;

import java.util.Date;

@Data
@Entity
@Table(name = "roles")
public class Role {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "role_id")
    private Long roleId;

    @Column(name = "type", nullable = false)
    private String type;

    @Column(name = "created_at")
    private Date createdDate;

    @Column(name = "updated_at")
    private Date updatedDate;

}
