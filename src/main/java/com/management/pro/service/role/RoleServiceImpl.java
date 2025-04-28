package com.management.pro.service.role;

import com.management.pro.config.SecurityContextHandler;
import com.management.pro.dtos.role.RoleRequest;
import com.management.pro.dtos.role.RoleResponse;
import com.management.pro.exceptions.ConflictException;
import com.management.pro.mapper.RoleMapper;
import com.management.pro.model.Permission;
import com.management.pro.model.Role;
import com.management.pro.repository.PermissionRepository;
import com.management.pro.repository.RoleRepository;
import com.management.pro.service.permission.PermissionService;
import com.management.pro.utils.AppMessagesProperties;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.*;
import java.util.stream.Collectors;

@Slf4j
@Service
@RequiredArgsConstructor
public class RoleServiceImpl implements RoleService {
    private final RoleRepository roleRepository;
    private final PermissionService permissionService;
    private final RoleMapper roleMapper;
    private final PermissionRepository permissionRepository;
    private final AppMessagesProperties appMessagesProperties;

    @Override
    public List<RoleResponse> findAll() {
        log.info("Find all roles");
        List<Role> roles = roleRepository.findAll();
        List<RoleResponse> roleDtos = roleMapper.toRoleDtos(roles);
        for (int i = 0; i < roleDtos.size(); i++) {
            RoleResponse roleResponse = roleDtos.get(i);
            List<Permission> permissions = roles.get(i).getPermissions();
            roleResponse.setPermissionsDto(extractPermissionNames(permissions));
        }
        return roleDtos;
    }


    private void ensureRoleDoesNotExist(String roleId) {
        roleRepository.findByRole(roleId)
                .ifPresent(existing -> {
                    throw new ConflictException(appMessagesProperties.getRoleAlreadyExists());
                });
    }

    private List<Permission> validatePermissionsExist(List<String> permissions) {
        List<Permission> existingPermissions = permissionService.getAllPermissions();


        List<String> existingPermissionNames  = existingPermissions
                .stream()
                .map(Permission::getPermission)
                .toList();

        boolean allExist = permissions.stream()
                .allMatch(existingPermissionNames::contains);

        if (!allExist) {
            throw new ConflictException(appMessagesProperties.getPermissionsDoNotExist());
        }

        return existingPermissions.stream()
                .filter(p -> permissions.contains(p.getPermission()))
                .collect(Collectors.toList());


    }

    public List<String> extractPermissionNames(List<Permission> permissions) {
        return permissions.stream()
                .map(Permission::getPermission)
                .toList();
    }





    @Override
    public RoleResponse create(RoleRequest roleAddRequest) {
        log.info("Begin create Role : {} ", roleAddRequest);
        ensureRoleDoesNotExist(roleAddRequest.getRole());
        List<Permission> permissions =   validatePermissionsExist(roleAddRequest.getPermissionsDto());
        Role role = roleMapper.toRole(roleAddRequest);
        role.setPermissions(permissions);
        Role savedModel = roleRepository.save(role);
        RoleResponse roleAddResponse =  roleMapper.toRoleDto(savedModel);
        roleAddResponse.setPermissionsDto(extractPermissionNames(permissions));
        return roleAddResponse;
    }


    @Override
    public RoleResponse updateRole(RoleRequest roleUpdateRequest) {
        log.info("Begin update  role : {}", roleUpdateRequest);
        Role role =  findRoleOrFail(roleUpdateRequest.getRole());
        roleMapper.updateEntity(role, roleUpdateRequest);
        List<Permission> permissions = validatePermissionsExist(roleUpdateRequest.getPermissionsDto());
        if(!permissions.isEmpty()) {
            role.setPermissions(permissions);
        }
        Role updatedRole = roleRepository.save(role);
        RoleResponse roleUpdateResponse =  roleMapper.toRoleDto(updatedRole);
        roleUpdateResponse.setPermissionsDto(extractPermissionNames(updatedRole.getPermissions()));
        log.info("End update  role : {}",roleUpdateResponse);
        return roleUpdateResponse;
    }



    @Override
    public void delete(String code) {
        roleRepository.deleteById(code);
    }

    @Override
    public List<Role> findByIdIn(List<String> ids) {
        //return roleRepository.findByIdIn(ids).orElse(List.of());
        return roleRepository.findByIdIn(ids);
    }


    @Override
    public RoleResponse findById(String roleId) {
        log.info("Begin findById : {}",roleId);
        Role role = roleRepository.findById(roleId)
                .orElseThrow(() -> new ConflictException(appMessagesProperties.getRoleNotFound() + roleId));
        RoleResponse response = roleMapper.toRoleDto(role);
        response.setPermissionsDto(extractPermissionNames(role.getPermissions()));
        return response;
    }

    private Role findRoleOrFail(String roleName) {
        return roleRepository.findByRole(roleName)
                .orElseThrow(() -> new ConflictException(appMessagesProperties.getRoleNotFound()));
    }

    private RoleResponse removePermissionAndSave(Role role, String permission) {
        Permission toRemove = role.getPermissions().stream()
                .filter(p -> p.getPermission().equals(permission))
                .findFirst()
                .orElseThrow(() -> new ConflictException(appMessagesProperties.getPermissionNotFoundInRole()));
        role.getPermissions().remove(toRemove);
        Role roleUpdated = roleRepository.save(role);
        RoleResponse roleResponse =  roleMapper.toRoleDto(roleUpdated);
        roleResponse.setPermissionsDto(extractPermissionNames(roleUpdated.getPermissions()));
        return roleResponse;
    }



    @Override
    public RoleResponse removePermissionFromRole(String roleId, String permission) {
        log.info("Begin removePermissionFromRole  , roleId = {}, permission = {}", roleId, permission);
        Role role = findRoleOrFail(roleId);
        return removePermissionAndSave(role, permission);
    }

    private RoleResponse addPermissionAndSave(Role role, String permission) {
        boolean permissionAlreadyExists = role.getPermissions().stream()
                .anyMatch(p -> p.getPermission().equals(permission));
        if (permissionAlreadyExists) {
            throw new ConflictException(appMessagesProperties.getPermissionAlreadyExistsInRole());
        }
        Permission permissionToSave = findPermissionOrFail(permission);
        role.getPermissions().add(permissionToSave);
        Role roleUpdated = roleRepository.save(role);
        RoleResponse roleResponse =  roleMapper.toRoleDto(roleUpdated);
        roleResponse.setPermissionsDto(extractPermissionNames(roleUpdated.getPermissions()));
        return roleResponse;
    }


    private Permission findPermissionOrFail(String permission) {
        return permissionRepository.findByPermission(permission)
                .orElseThrow(() -> new ConflictException(appMessagesProperties.getPermissionNotFound() + permission));
    }

    @Override
    public RoleResponse addPermissionToRole(String roleId, String permission) {
        log.info("Begin addPermissionToRole  , roleId = {}, permission = {}", roleId, permission);
        Role role = findRoleOrFail(roleId);
        return addPermissionAndSave(role, permission);
    }

    @Override
    public String getCurrentRole() {
        List<String> roleList = Optional.ofNullable(SecurityContextHandler.getRoles()).orElse(Collections.emptyList());
        log.info("roleList : {}",roleList);
        List<Role> roleModelList = Optional.ofNullable(findByIdIn(List.copyOf(roleList))).orElse(List.of());
        log.info("roleList : {}",roleModelList);
        return roleModelList.stream()
                .findFirst()
                .map(Role::getRole)
                .orElse("");
    }


}
