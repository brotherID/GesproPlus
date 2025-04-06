package com.management.pro.config;

import com.management.pro.model.RoleModel;
import com.management.pro.service.RoleService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.Collections;
import java.util.List;
import java.util.Optional;

@Service("roleAccessHandler")
@RequiredArgsConstructor
@Slf4j
public class RoleAccessHandlerImpl implements RoleAccessHandler {

    private final RoleService roleService;

    private final SecurityConfigProperties securityConfigProperties;

    @Override
    public Boolean hasPermission(String permission) {
        log.info("Checking if role access has permission: " + permission);
        if (Boolean.FALSE.equals(securityConfigProperties.getEnabled())) {
            return true;
        }
        List<String> roleList = Optional.ofNullable(SecurityContextHandler.getRoles()).orElse(Collections.emptyList());
        log.info("roleList : " + roleList);
        List<RoleModel> roleModelList = Optional.ofNullable(this.roleService.findByIdIn(List.copyOf(roleList))).orElse(List.of());
        log.info("roleModelList : " + roleModelList);
        return roleModelList.stream()
                .flatMap(role -> role.getPermissionList().stream())
                .anyMatch(code -> code.equals(permission));
    }


    @Override
    public Boolean hasRole(String role) {
        List<String> roleList = Optional.ofNullable(SecurityContextHandler.getRoles()).orElse(Collections.emptyList());
        List<RoleModel> roleModelList = Optional.ofNullable(this.roleService.findByIdIn(List.copyOf(roleList))).orElse(List.of());
        return roleModelList.stream()
                .map(RoleModel::getId)
                .anyMatch(code -> code.equals(role));
    }
}
