package com.management.pro.service.permission;

import com.management.pro.model.PermissionModel;
import com.management.pro.repository.PermissionRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class PermissionServiceImpl implements PermissionService {

    private final PermissionRepository permissionRepository;

    @Override
    public List<PermissionModel> getAllPermissions() {
        return permissionRepository.findAll();
    }

    @Override
    public PermissionModel getPermissionById(String id) {
        return permissionRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Permission not found with id: " + id));
    }

    @Override
    public PermissionModel createPermission(PermissionModel permission) {
        permissionRepository.findById(permission.getId())
                .ifPresent(permissionFounded -> {
                    throw new RuntimeException("Permission already exists");
                });
        return permissionRepository.save(permission);
    }

    @Override
    public PermissionModel updatePermission(PermissionModel permission) {
        PermissionModel existingPermission = permissionRepository.findById(permission.getId())
                .orElseThrow(() -> new RuntimeException("Permission not found with id: " + permission.getId()));

        PermissionModel updatedPermission = PermissionModel.builder()
                .id(existingPermission.getId())
                .permission(permission.getPermission() != null ? permission.getPermission() : existingPermission.getPermission())
                .build();

        return permissionRepository.save(updatedPermission);
    }

    @Override
    public void deletePermission(String id) {
        if (!permissionRepository.existsById(id)) {
            throw new RuntimeException("permission not found with id: " + id);
        }
        permissionRepository.deleteById(id);
    }
}
