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
    public ResponseEntity<Role> createRole(Role roleToSave) {
        return ResponseEntity.ok(roleService.create(roleToSave));
    }


    @Override
    public ResponseEntity<Role> updateRole(Role role) {
        RoleModel roleModel = roleMapper.toRoleModel(role);
        RoleModel savedRoleModel = roleService.save(roleModel);
        return ResponseEntity.ok().body(roleMapper.toRole(savedRoleModel));
    }

    @Override
    public ResponseEntity<Void> deleteRole(String id) {
        roleService.delete(id);
        return ResponseEntity.noContent().build();
    }





}
