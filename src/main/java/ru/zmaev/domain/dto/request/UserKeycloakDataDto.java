package ru.zmaev.domain.dto.request;

import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class UserKeycloakDataDto {
    private String keycloakUID;
    private String username;
    private String email;
    private String roleName;
}
