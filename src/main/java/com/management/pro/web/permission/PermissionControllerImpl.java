package com.management.pro.web.permission;

import com.management.pro.dtos.Permission;
import com.management.pro.mapper.PermissionMapper;
import com.management.pro.model.PermissionModel;
import com.management.pro.service.permission.PermissionService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequiredArgsConstructor
public class PermissionControllerImpl implements PermissionController {

    private final PermissionService permissionService;
    private final PermissionMapper permissionMapper;

    @Override
    public ResponseEntity<List<Permission>> getAllPermissions() {
        List<PermissionModel> permissions = permissionService.getAllPermissions();
        return ResponseEntity.ok(permissionMapper.toPermissionList(permissions));
    }

    @Override
    public ResponseEntity<Permission> getPermissionById(String id) {
        PermissionModel permission = permissionService.getPermissionById(id);
        return ResponseEntity.ok(permissionMapper.toPermission(permission));
    }

    @Override
    public ResponseEntity<Permission> createPermission(Permission permissionDTO) {
        PermissionModel permission = permissionMapper.toPermissionModel(permissionDTO);
        PermissionModel createdPermission = permissionService.createPermission(permission);
        return ResponseEntity.ok(permissionMapper.toPermission(createdPermission));
    }

    @Override
    public ResponseEntity<Permission> updatePermission(Permission permissionDTO) {
        PermissionModel permission = permissionMapper.toPermissionModel(permissionDTO);
        PermissionModel updatedPermission = permissionService.updatePermission(permission);
        return ResponseEntity.ok(permissionMapper.toPermission(updatedPermission));
    }

    @Override
    public ResponseEntity<Void> deletePermission(String id) {
        permissionService.deletePermission(id);
        return ResponseEntity.ok().build();
    }
}
