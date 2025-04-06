package com.management.pro.model;

import jakarta.persistence.*;
import lombok.*;

import java.util.List;

@Getter
@Setter
@ToString
@Entity(name = "role")
@AllArgsConstructor
@NoArgsConstructor
public class RoleModel {
    @Id
    private String id;
    private String role;
    @ElementCollection(fetch = FetchType.EAGER)
    @CollectionTable(name = "roles_permissions", joinColumns = @JoinColumn(name = "role_id", referencedColumnName = "id"))
    @Column(name = "permission")
    private List<String> permissionList;
}
