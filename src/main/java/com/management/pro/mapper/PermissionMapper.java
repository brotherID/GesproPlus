package com.management.pro.mapper;

import com.management.pro.dtos.permission.PermissionRequest;
import com.management.pro.dtos.permission.PermissionResponse;
import com.management.pro.model.Permission;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

import java.util.List;

@Mapper(componentModel = "spring")
public interface PermissionMapper {

    PermissionResponse toPermissionDto(Permission permission);

    @Mapping(target = "id", source = "permission")
    Permission toPermission(PermissionRequest permissionRequest);

    List<PermissionResponse> toPermissionDtos(List<Permission> permissions);


}
