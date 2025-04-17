package com.management.pro.web.role;

import com.management.pro.dtos.Role;
import com.management.pro.mapper.RoleMapper;
import com.management.pro.model.RoleModel;
import com.management.pro.service.role.RoleService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;
import java.util.Optional;

@RestController
@RequiredArgsConstructor
@Slf4j
public class RoleControllerImpl implements RoleController {
    private final RoleService roleService;
    private final RoleMapper roleMapper;


    @Override
    public ResponseEntity<List<Role>> getAllRoles() {
        List<RoleModel> roleModels = roleService.findAll();
        List<Role> roleList = roleMapper.toRoleList(roleModels);
        return ResponseEntity.ok().body(roleList);
    }

    @Override
    public ResponseEntity<Optional<Role>> getRoleById(String id) {
        return ResponseEntity.ok().body(roleService.findById(id));
    }


    @Override
    public ResponseEntity<Role> createRole(Role roleToSave) {
        return ResponseEntity.ok(roleService.create(roleToSave));
    }


    @Override
    public ResponseEntity<Role> updateRole(Role role) {
        return ResponseEntity.ok().body(roleService.updateRole(role));
    }

    @Override
    public ResponseEntity<Void> deleteRole(String id) {
        roleService.delete(id);
        return ResponseEntity.noContent().build();
    }

    @Override
    public ResponseEntity<Role> removePermissionFromRole(String roleId, String permission) {
        return ResponseEntity.ok(roleService.removePermissionFromRole(roleId, permission));
    }

    @Override
    public ResponseEntity<Role> addPermissionToRole(String roleId, String permission) {
        return ResponseEntity.ok(roleService.addPermissionToRole(roleId, permission));
    }


}
