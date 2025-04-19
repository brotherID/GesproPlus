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

import java.util.Collections;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

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
        ensureRoleDoesNotExist(role.getId());
        validatePermissionsExist(role.getPermissionList());
        RoleModel savedModel = roleRepository.save(roleMapper.toRoleModel(role));
        return roleMapper.toRole(savedModel);
    }


    @Override
    public Role updateRole(Role role) {
        log.info("Begin update  role : {}", role);
        RoleModel roleModel =  findRoleOrFail(role.getId());
        roleModel.setIsAdmin(role.getIsAdmin());
        roleModel.setIsSuperAdmin(role.getIsSuperAdmin());
        roleModel.setRole(role.getRole());
        roleModel.setPermissionList( role.getPermissionList().stream().distinct().collect(Collectors.toList()));
        return roleMapper.toRole(roleRepository.save(roleModel));
    }

    @Override
    public void delete(String code) {
        roleRepository.deleteById(code);
    }

    @Override
    public List<RoleModel> findByIdIn(List<String> ids) {
        return roleRepository.findByIdIn(ids).orElse(List.of());
    }

    @Override
    public Optional<Role> findById(String roleId) {
        return roleRepository.findById(roleId)
                .map(roleMapper::toRole);
    }

    private RoleModel findRoleOrFail(String roleId) {
        return roleRepository.findById(roleId)
                .orElseThrow(() -> new ConflictException("Role not found"));
    }

    private Role removePermissionAndSave(RoleModel role, String permission) {
        boolean removed = role.getPermissionList().remove(permission);
        if (!removed) {
            throw new ConflictException("Permission not found in role");
        }

        RoleModel updated = roleRepository.save(role);
        return roleMapper.toRole(updated);
    }



    @Override
    public Role removePermissionFromRole(String roleId, String permission) {
        log.info("Begin removePermissionFromRole  , roleId = {}, permission = {}", roleId, permission);
        RoleModel role = findRoleOrFail(roleId);
        return removePermissionAndSave(role, permission);
    }

    private Role addPermissionAndSave(RoleModel role, String permission) {
        if (role.getPermissionList().contains(permission)) {
            throw new ConflictException("Permission already exists in role");
        }

        role.getPermissionList().add(permission);
        RoleModel updated = roleRepository.save(role);
        return roleMapper.toRole(updated);
    }

    @Override
    public Role addPermissionToRole(String roleId, String permission) {
        log.info("Begin addPermissionToRole  , roleId = {}, permission = {}", roleId, permission);
        RoleModel role = findRoleOrFail(roleId);
        return addPermissionAndSave(role, permission);
    }

    @Override
    public String getCurrentRole() {
        List<String> roleList = Optional.ofNullable(SecurityContextHandler.getRoles()).orElse(Collections.emptyList());
        List<RoleModel> roleModelList = Optional.ofNullable(findByIdIn(List.copyOf(roleList))).orElse(List.of());
        return roleModelList.stream()
                .findFirst()
                .map(RoleModel::getRole)
                .orElse("");
    }


}
