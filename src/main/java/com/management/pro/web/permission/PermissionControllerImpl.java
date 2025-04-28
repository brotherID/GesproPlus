package com.management.pro.web.permission;

import com.management.pro.dtos.permission.PermissionRequest;
import com.management.pro.dtos.permission.PermissionResponse;
import com.management.pro.mapper.PermissionMapper;
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
    public ResponseEntity<List<PermissionResponse>> getAllPermissions() {
        return ResponseEntity.ok(permissionMapper.toPermissionDtos(permissionService.getAllPermissions()));
    }



    @Override
    public ResponseEntity<PermissionResponse> getPermissionById(String id) {
        return ResponseEntity.ok(permissionService.getPermissionById(id));
    }

    @Override
    public ResponseEntity<PermissionResponse> createPermission(PermissionRequest permissionRequest) {
        return ResponseEntity.ok(permissionService.createPermission(permissionRequest));
    }

    @Override
    public ResponseEntity<PermissionResponse> updatePermission(String id ,  PermissionRequest permissionRequest) {
        return ResponseEntity.ok(permissionService.updatePermission(id,permissionRequest));
    }

    @Override
    public ResponseEntity<Void> deletePermission(String id) {
        permissionService.deletePermission(id);
        return ResponseEntity.noContent().build();
    }
}
