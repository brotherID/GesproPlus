package com.management.pro.service.role;

import com.management.pro.dtos.Role;
import com.management.pro.exceptions.ConflictException;
import com.management.pro.mapper.RoleMapper;
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

    @Override
    public Role create(Role role) {
         log.info("Begin create Role");
         roleRepository.findById(role.getId())
                .ifPresent(roleFounded -> {
                    throw new ConflictException(ROLE_ALREADY_EXISTS);
                });

        boolean allExist = role.getPermissionList()
                .stream()
                .allMatch(rolePermission -> permissionService.getAllPermissions()
                        .stream()
                        .anyMatch(existingPermission -> Objects.equals(existingPermission.getPermission(), rolePermission)
                )
        );

        if(!allExist) {
            throw new ConflictException("Permissions do not exists");
        }

        RoleModel roleModel = roleMapper.toRoleModel(role);
        return roleMapper.toRole(roleRepository.save(roleModel));
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
