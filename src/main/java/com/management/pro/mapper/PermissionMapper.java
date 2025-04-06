package com.management.pro.mapper;

import com.management.pro.dtos.Permission;
import com.management.pro.model.PermissionModel;
import org.mapstruct.Mapper;

import java.util.List;

@Mapper(componentModel = "spring")
public interface PermissionMapper {

    Permission toPermission(PermissionModel permissionModel);

    PermissionModel toPermissionModel(Permission permission);

    List<Permission> toPermissionList(List<PermissionModel> permissionModelList);

}
