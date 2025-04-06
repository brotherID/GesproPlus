package com.management.pro.service;

import com.management.pro.dtos.Role;
import com.management.pro.mapper.RoleMapper;
import com.management.pro.model.RoleModel;
import com.management.pro.repository.RoleRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.List;

@Slf4j
@Service
@RequiredArgsConstructor
public class RoleServiceImpl implements RoleService {
    private final RoleRepository roleRepository;
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
                    throw new RuntimeException("Role already exists");
                });
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
