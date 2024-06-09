package org.araa.dto;

import lombok.Builder;
import lombok.Data;
import org.araa.domain.User;


@Builder
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
