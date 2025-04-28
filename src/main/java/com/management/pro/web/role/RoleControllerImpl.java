package com.management.pro.web.role;

import com.management.pro.dtos.role.RoleRequest;
import com.management.pro.dtos.role.RoleResponse;
import com.management.pro.service.role.RoleService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequiredArgsConstructor
@Slf4j
public class RoleControllerImpl implements RoleController {
    private final RoleService roleService;



    @Override
    public ResponseEntity<List<RoleResponse>> getAllRoles() {
        return ResponseEntity.ok().body(roleService.findAll());
    }

    @Override
    public ResponseEntity<RoleResponse> getRoleById(String id) {
        return ResponseEntity.ok().body(roleService.findById(id));
    }


    @Override
    public ResponseEntity<RoleResponse> createRole(RoleRequest roleAddRequest) {
        return ResponseEntity.ok(roleService.create(roleAddRequest));
    }


    @Override
    public ResponseEntity<RoleResponse> updateRole(RoleRequest roleUpdateRequest) {
        return ResponseEntity.ok().body(roleService.updateRole(roleUpdateRequest));
    }

    @Override
    public ResponseEntity<Void> deleteRole(String id) {
        roleService.delete(id);
        return ResponseEntity.noContent().build();
    }

    @Override
    public ResponseEntity<RoleResponse> removePermissionFromRole(String roleId, String permission) {
        return ResponseEntity.ok(roleService.removePermissionFromRole(roleId, permission));
    }

    @Override
    public ResponseEntity<RoleResponse> addPermissionToRole(String roleId, String permission) {
        return ResponseEntity.ok(roleService.addPermissionToRole(roleId, permission));
    }

    @Override
    public ResponseEntity<String> getCurrentRole() {
        return ResponseEntity.ok(roleService.getCurrentRole());
    }


}
