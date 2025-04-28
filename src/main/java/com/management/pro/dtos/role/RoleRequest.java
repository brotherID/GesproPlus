package com.management.pro.dtos.role;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.ArrayList;
import java.util.List;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class RoleRequest {
    private String role;
    private Boolean isAdmin;
    private Boolean isSuperAdmin;
    private List<String> permissionsDto = new ArrayList<>();
}
