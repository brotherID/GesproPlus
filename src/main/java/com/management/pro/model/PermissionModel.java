package com.management.pro.model;

import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import lombok.*;

@Getter
@Setter
@ToString
@Entity(name = "permission")
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class PermissionModel {
    @Id
    private String id;
    private String permission;
}
