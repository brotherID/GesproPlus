package com.management.pro.service.permission;

import com.management.pro.model.PermissionModel;

import java.util.List;

public interface PermissionService {
    List<PermissionModel> getAllPermissions();

    PermissionModel getPermissionById(String id);

    PermissionModel createPermission(PermissionModel permission);

    PermissionModel updatePermission(PermissionModel permission);

    void deletePermission(String id);

}
