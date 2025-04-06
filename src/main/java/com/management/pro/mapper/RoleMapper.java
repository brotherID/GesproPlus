package com.management.pro.mapper;

import com.management.pro.dtos.Role;
import com.management.pro.model.RoleModel;
import org.mapstruct.Mapper;

import java.util.List;

@Mapper(componentModel = "spring")
public interface RoleMapper {


    RoleModel toRoleModel(Role role);

    Role toRole(RoleModel roleModel);

    List<Role> toRoleList(List<RoleModel> roleModelList);
}
