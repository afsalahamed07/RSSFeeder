package org.araa.dto;

import lombok.Builder;
import lombok.Data;


@Builder
@Data
public class UserRegistrationResponseDTO {
    private String username;
    private String name;
    private String email;
}
