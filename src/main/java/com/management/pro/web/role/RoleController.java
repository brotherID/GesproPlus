package com.management.pro.web.role;

import com.management.pro.dtos.Role;
import com.management.pro.tools.Permissions;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RequestMapping("/api/v1/settings/role")
@CrossOrigin("*")
public interface RoleController {

    @GetMapping
    @PreAuthorize("@roleAccessHandler.hasPermission(\"" + Permissions.ROLE_GET_PERMISSION + "\")")
    ResponseEntity<List<Role>> getAllRoles();

    @PostMapping
    @PreAuthorize("@roleAccessHandler.hasPermission(\"" + Permissions.ROLE_ADD_PERMISSION + "\")")
    ResponseEntity<Role> createRole(@RequestBody Role roleDTO);

    @PutMapping
    @PreAuthorize("@roleAccessHandler.hasPermission(\"" + Permissions.ROLE_PUT_PERMISSION + "\")")
    ResponseEntity<Role> updateRole(@RequestBody Role roleDTO);

    @DeleteMapping("/{id}")
    @PreAuthorize("@roleAccessHandler.hasPermission(\"" + Permissions.ROLE_DELETE_PERMISSION + "\")")
    ResponseEntity<Void> deleteRole(@PathVariable String id);
}
