package com.management.pro.service.permission;

import com.management.pro.dtos.permission.PermissionRequest;
import com.management.pro.dtos.permission.PermissionResponse;
import com.management.pro.model.Permission;

import java.util.List;

public interface PermissionService {
    List<Permission> getAllPermissions();

    PermissionResponse getPermissionById(String id);

    PermissionResponse createPermission(PermissionRequest permissionRequest);

    PermissionResponse updatePermission(String idPermission ,PermissionRequest permissionRequest);

    void deletePermission(String id);

}
