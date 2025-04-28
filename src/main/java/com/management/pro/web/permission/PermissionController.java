package com.management.pro.web.permission;

import com.management.pro.dtos.permission.PermissionRequest;
import com.management.pro.dtos.permission.PermissionResponse;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RequestMapping("/api/v1/settings/permission")
@CrossOrigin("*")
public interface PermissionController {

    @GetMapping
    @PreAuthorize("@roleAccessHandler.hasPermission(T(com.management.pro.enums.PermissionEnum).PERMISSION_GET)")
    ResponseEntity<List<PermissionResponse>> getAllPermissions();

    @GetMapping("/{id}")
    @PreAuthorize("@roleAccessHandler.hasPermission(T(com.management.pro.enums.PermissionEnum).PERMISSION_GET)")
    ResponseEntity<PermissionResponse> getPermissionById(@PathVariable String id);

    @PostMapping
    @PreAuthorize("@roleAccessHandler.hasPermission(T(com.management.pro.enums.PermissionEnum).PERMISSION_ADD)")
    ResponseEntity<PermissionResponse> createPermission(@RequestBody PermissionRequest permissionRequest);

    @PutMapping("/{id}")
    @PreAuthorize("@roleAccessHandler.hasPermission(T(com.management.pro.enums.PermissionEnum).PERMISSION_PUT)")
    ResponseEntity<PermissionResponse> updatePermission(@PathVariable String id ,  @RequestBody PermissionRequest permissionRequest);

    @DeleteMapping("/{id}")
    @PreAuthorize("@roleAccessHandler.hasPermission(T(com.management.pro.enums.PermissionEnum).PERMISSION_DELETE)")
    ResponseEntity<Void> deletePermission(@PathVariable String id);
}
