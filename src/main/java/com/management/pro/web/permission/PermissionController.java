package com.management.pro.web.permission;

import com.management.pro.dtos.Permission;
import com.management.pro.tools.Permissions;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RequestMapping("/api/v1/settings/permission")
@CrossOrigin("*")
public interface PermissionController {

    @GetMapping
    @PreAuthorize("@roleAccessHandler.hasPermission(\"" + Permissions.PERMISSION_GET_PERMISSION + "\")")
    ResponseEntity<List<Permission>> getAllPermissions();

    @GetMapping("/{id}")
    @PreAuthorize("@roleAccessHandler.hasPermission(\"" + Permissions.PERMISSION_GET_PERMISSION + "\")")
    ResponseEntity<Permission> getPermissionById(@PathVariable String id);

    @PostMapping
    @PreAuthorize("@roleAccessHandler.hasPermission(\"" + Permissions.PERMISSION_ADD_PERMISSION + "\")")
    ResponseEntity<Permission> createPermission(@RequestBody Permission permission);

    @PutMapping
    @PreAuthorize("@roleAccessHandler.hasPermission(\"" + Permissions.PERMISSION_PUT_PERMISSION + "\")")
    ResponseEntity<Permission> updatePermission(@RequestBody Permission permission);

    @DeleteMapping("/{id}")
    @PreAuthorize("@roleAccessHandler.hasPermission(\"" + Permissions.PERMISSION_DELETE_PERMISSION + "\")")
    ResponseEntity<Void> deletePermission(@PathVariable String id);
}
