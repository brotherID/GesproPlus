package com.management.pro.service.role;

import com.management.pro.config.SecurityContextHandler;
import com.management.pro.dtos.Role;
import com.management.pro.exceptions.ConflictException;
import com.management.pro.mapper.RoleMapper;
import com.management.pro.model.PermissionModel;
import com.management.pro.model.RoleModel;
import com.management.pro.repository.RoleRepository;
import com.management.pro.service.permission.PermissionService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Objects;

@Slf4j
@Service
@RequiredArgsConstructor
public class RoleServiceImpl implements RoleService {
    public static final String ROLE_ALREADY_EXISTS = "Role already exists";
    private final RoleRepository roleRepository;
    private final PermissionService permissionService;
    private final RoleMapper roleMapper;

    @Override
    public List<RoleModel> findAll() {
        return roleRepository.findAll();
    }


    public boolean canCreateRole(String currentRole, String roleToSave) {
        if ("SUPERADMIN".equals(currentRole)) {
            return "ADMIN".equals(roleToSave);
        }
        if ("ADMIN".equals(currentRole)) {
            return !List.of("ADMIN", "SUPERADMIN").contains(roleToSave);
        }
        return false;
    }

    public String getCurrentRole() {
        if (SecurityContextHandler.getRoles().contains("SUPERADMIN")) {
            return "SUPERADMIN";
        } else if (SecurityContextHandler.getRoles().contains("ADMIN")) {
            return "ADMIN";
        }else{
            return "";
        }
    }

    private void validateAuthorizationToCreate(Role role) {
        if (!canCreateRole(getCurrentRole(), role.getRole())) {
            throw new ConflictException("You do not have the right to create this role.");
        }
    }

    private void ensureRoleDoesNotExist(String roleId) {
        roleRepository.findById(roleId)
                .ifPresent(existing -> {
                    throw new ConflictException(ROLE_ALREADY_EXISTS);
                });
    }

    private void validatePermissionsExist(List<String> permissionList) {
        List<String> existingPermissions = permissionService.getAllPermissions()
                .stream()
                .map(PermissionModel::getPermission)
                .toList();

        boolean allExist = permissionList.stream()
                .allMatch(existingPermissions::contains);

        if (!allExist) {
            throw new ConflictException("Permissions do not exist");
        }
    }


    @Override
    public Role create(Role role) {
        log.info("Begin create Role");
//        if (canCreateRole(getCurrentRole(), role.getRole())) {
//            roleRepository.findById(role.getId())
//                    .ifPresent(roleFounded -> {
//                        throw new ConflictException(ROLE_ALREADY_EXISTS);
//                    });
//
//            boolean allExist = role.getPermissionList()
//                    .stream()
//                    .allMatch(rolePermission -> permissionService.getAllPermissions()
//                            .stream()
//                            .anyMatch(existingPermission -> Objects.equals(existingPermission.getPermission(), rolePermission)
//                            )
//                    );
//
//            if (!allExist) {
//                throw new ConflictException("Permissions do not exists");
//            }
//
//            RoleModel roleModel = roleMapper.toRoleModel(role);
//            return roleMapper.toRole(roleRepository.save(roleModel));
//        }else{
//            throw new ConflictException("You do not have the right to create this role.");
//        }
        validateAuthorizationToCreate(role);
        ensureRoleDoesNotExist(role.getId());
        validatePermissionsExist(role.getPermissionList());

        RoleModel savedModel = roleRepository.save(roleMapper.toRoleModel(role));
        return roleMapper.toRole(savedModel);

    }


    @Override
    public RoleModel save(RoleModel roleModel) {
        roleRepository.findById(roleModel.getId()).orElseThrow(RuntimeException::new);
        return roleRepository.save(roleModel);
    }

    @Override
    public void delete(String code) {
        roleRepository.deleteById(code);
    }

    @Override
    public List<RoleModel> findByIdIn(List<String> ids) {
        return roleRepository.findByIdIn(ids).orElse(List.of());
    }
}
