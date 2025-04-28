package com.management.pro.service.role;

import com.management.pro.dtos.role.RoleRequest;
import com.management.pro.dtos.role.RoleResponse;
import com.management.pro.model.Role;

import java.util.List;


public interface RoleService {
    List<RoleResponse> findAll();

    RoleResponse updateRole(RoleRequest roleUpdateRequest);

    RoleResponse create(RoleRequest roleAddRequest);

    void delete(String id);

    List<Role> findByIdIn(List<String> id);

    RoleResponse findById(String id);

    RoleResponse removePermissionFromRole(String roleId, String permission);

    RoleResponse addPermissionToRole(String roleId, String permission);

    String getCurrentRole();
}
