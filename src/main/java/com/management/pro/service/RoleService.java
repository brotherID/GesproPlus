package com.management.pro.service;

import com.management.pro.dtos.Role;
import com.management.pro.model.RoleModel;

import java.util.List;


public interface RoleService {
    List<RoleModel> findAll();

    RoleModel save(RoleModel roleModel);

    Role create(Role roleModel);

    void delete(String id);

    List<RoleModel> findByIdIn(List<String> id);
}
