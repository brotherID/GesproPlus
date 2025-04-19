package com.management.pro.service.role;

import com.management.pro.dtos.Role;
import com.management.pro.model.RoleModel;

import java.util.List;
import java.util.Optional;


public interface RoleService {
    List<RoleModel> findAll();

    Role updateRole(Role role);

    Role create(Role roleModel);

    void delete(String id);

    List<RoleModel> findByIdIn(List<String> id);

    Optional<Role> findById(String id);

    Role removePermissionFromRole(String roleId, String permission);

    Role addPermissionToRole(String roleId, String permission);

    String getCurrentRole();
}
