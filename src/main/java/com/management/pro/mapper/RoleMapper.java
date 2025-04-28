package com.management.pro.mapper;

import com.management.pro.dtos.role.RoleRequest;
import com.management.pro.dtos.role.RoleResponse;
import com.management.pro.model.Role;
import org.mapstruct.*;

import java.util.List;

@Mapper(componentModel = "spring")
public interface RoleMapper {

    @Mapping(target = "permissions", ignore = true)
    @Mapping(target = "id", source = "role")
    Role toRole(RoleRequest roleAddRequest);

    @Mapping(target = "permissionsDto", ignore = true)
    @Mapping(target = "role", source = "id")
    RoleResponse toRoleDto(Role role);

    @Mapping(target = "permissions", ignore = true)
    List<RoleResponse> toRoleDtos(List<Role> roles);

    @Mapping(target = "permissionsDto", ignore = true)
    List<Role> toRoles(List<RoleRequest> roleAddRequest);

    @BeanMapping(nullValuePropertyMappingStrategy = NullValuePropertyMappingStrategy.IGNORE)
    void updateEntity(@MappingTarget Role role, RoleRequest roleRequest);
}
