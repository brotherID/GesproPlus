package com.management.pro.dtos.user;

import com.management.pro.model.Role;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.ArrayList;
import java.util.List;


@Data
@NoArgsConstructor
@AllArgsConstructor
public class UserAccountResponse {
    private String idUserAccount;
    private String username;
    private boolean enabled;
    private String firstName;
    private String lastName;
    private String email;
    private boolean emailVerified;
    private List<Role> roles = new ArrayList<>();
    private String password;
}
