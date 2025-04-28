package com.management.pro.dtos.user;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class UserKeycloak {
    private String username;
    private boolean enabled;
    private String firstName;
    private String lastName;
    private String email;
    private boolean emailVerified;
    private List<CredentialDto> credentials;
}
