package org.araa.domain;

import jakarta.persistence.*;
import lombok.Data;

import java.util.List;


@Data
@Entity
@Table(name = "profiles")
public class Profile {
    @Id
    private String username;

    @ElementCollection(fetch = FetchType.LAZY)
    private List<String> subscriptions;
}
