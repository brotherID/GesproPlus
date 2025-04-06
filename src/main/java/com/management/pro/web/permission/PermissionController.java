package com.management.pro.web.permission;

import com.management.pro.dtos.Permission;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RequestMapping("/api/v1/settings/permission")
@CrossOrigin("*")
public interface PermissionController {

    @GetMapping
    ResponseEntity<List<Permission>> getAllPermissions();

    @GetMapping("/{id}")
    ResponseEntity<Permission> getPermissionById(@PathVariable String id);

    @PostMapping
    ResponseEntity<Permission> createPermission(@RequestBody Permission permission);

    @PutMapping
    ResponseEntity<Permission> updatePermission(@RequestBody Permission permission);

    @DeleteMapping("/{id}")
    ResponseEntity<Void> deletePermission(@PathVariable String id);
}
