package com.management.pro.web.role;

import com.management.pro.dtos.role.RoleRequest;
import com.management.pro.dtos.role.RoleResponse;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RequestMapping("/api/v1/settings/role")
@CrossOrigin("*")
public interface RoleController {

    @GetMapping
    @PreAuthorize("@roleAccessHandler.hasPermission(T(com.management.pro.enums.PermissionEnum).ROLE_GET)")
    ResponseEntity<List<RoleResponse>> getAllRoles();

    @GetMapping("/{id}")
    @PreAuthorize("@roleAccessHandler.hasPermission(T(com.management.pro.enums.PermissionEnum).ROLE_GET)")
    ResponseEntity<RoleResponse> getRoleById(@PathVariable String id);

    @PostMapping
    @PreAuthorize("@roleAccessHandler.hasPermission(T(com.management.pro.enums.PermissionEnum).ROLE_ADD)")
    ResponseEntity<RoleResponse> createRole(@RequestBody RoleRequest roleAddRequest);

    @PutMapping
    @PreAuthorize("@roleAccessHandler.hasPermission(T(com.management.pro.enums.PermissionEnum).ROLE_PUT)")
    ResponseEntity<RoleResponse> updateRole(@RequestBody RoleRequest roleUpdateRequest);

    @DeleteMapping("/{id}")
    @PreAuthorize("@roleAccessHandler.hasPermission(T(com.management.pro.enums.PermissionEnum).ROLE_DELETE)")
    ResponseEntity<Void> deleteRole(@PathVariable String id);

    @DeleteMapping("/{roleId}/permissions/{permission}")
    @PreAuthorize("@roleAccessHandler.hasPermission(T(com.management.pro.enums.PermissionEnum).ROLE_DELETE)")
    ResponseEntity<RoleResponse> removePermissionFromRole(@PathVariable String roleId,
                                                 @PathVariable String permission);

    @PutMapping("/{roleId}/permissions/{permission}")
    @PreAuthorize("@roleAccessHandler.hasPermission(T(com.management.pro.enums.PermissionEnum).ROLE_PUT)")
    ResponseEntity<RoleResponse> addPermissionToRole(
            @PathVariable String roleId,
            @PathVariable String permission);

    @GetMapping("/current")
    ResponseEntity<String> getCurrentRole();
}
