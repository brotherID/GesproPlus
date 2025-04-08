package com.management.pro.service.permission;

import com.management.pro.exceptions.ConflictException;
import com.management.pro.exceptions.NotFoundException;
import com.management.pro.model.PermissionModel;
import com.management.pro.repository.PermissionRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
@Slf4j
public class PermissionServiceImpl implements PermissionService {

    public static final String PERMISSION_NOT_FOUND_WITH_ID = "Permission not found with id :";
    public static final String PERMISSION_ALREADY_EXISTS = "Permission already exists";
    private final PermissionRepository permissionRepository;

    @Override
    public List<PermissionModel> getAllPermissions() {
        return permissionRepository.findAll();
    }

    @Override
    public PermissionModel getPermissionById(String id) {
        return permissionRepository.findById(id)
                .orElseThrow(() -> new NotFoundException(PERMISSION_NOT_FOUND_WITH_ID  + id));
    }

    @Override
    public PermissionModel createPermission(PermissionModel permission) {
        permissionRepository.findById(permission.getId())
                .ifPresent(permissionFounded -> {
                    throw new ConflictException(PERMISSION_ALREADY_EXISTS);
                });
        return permissionRepository.save(permission);
    }

    @Override
    public PermissionModel updatePermission(PermissionModel permission) {
        log.info("Begin update permission : {}", permission);
        PermissionModel existingPermission = permissionRepository.findById(permission.getId())
                .orElseThrow(() -> new NotFoundException(PERMISSION_NOT_FOUND_WITH_ID + " " + permission.getId()));

        PermissionModel updatedPermission = PermissionModel.builder()
                .id(existingPermission.getId())
                .permission(permission.getPermission() != null ? permission.getPermission() : existingPermission.getPermission())
                .build();

        return permissionRepository.save(updatedPermission);
    }

    @Override
    public void deletePermission(String id) {
        if (!permissionRepository.existsById(id)) {
            throw new NotFoundException(PERMISSION_NOT_FOUND_WITH_ID + id);
        }
        permissionRepository.deleteById(id);
    }
}
