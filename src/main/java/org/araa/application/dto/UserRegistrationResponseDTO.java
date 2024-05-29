package org.araa.application.dto;

import lombok.Data;
import org.araa.domain.User;


@Data
public class UserRegistrationResponseDTO {
    private String username;
    private String name;
    private String email;

    public UserRegistrationResponseDTO( User user ) {
        this.username = user.getUsername();
        this.name = user.getName();
        this.email = user.getEmail();
    }
}
