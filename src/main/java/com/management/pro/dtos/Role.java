package com.management.pro.dtos;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class Role {
    private String id;
    private String role;
    private Boolean isAdmin;
    private List<String> permissionList;
}
