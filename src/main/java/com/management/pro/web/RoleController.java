package com.management.pro.web;

import com.management.pro.dtos.Role;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RequestMapping("/api/v1/settings/role")
@CrossOrigin("*")
public interface RoleController {

    @GetMapping
    ResponseEntity<List<Role>> getAllRoles();

    @PostMapping
    ResponseEntity<Role> createRole(@RequestBody Role roleDTO);

    @PutMapping
    ResponseEntity<Role> updateRole(@RequestBody Role roleDTO);

    @DeleteMapping("/{id}")
    ResponseEntity<Void> deleteRole(@PathVariable String id);
}
