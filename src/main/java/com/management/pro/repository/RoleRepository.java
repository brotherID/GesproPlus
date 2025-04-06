package com.management.pro.repository;

import com.management.pro.model.RoleModel;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface RoleRepository extends JpaRepository<RoleModel, String> {
    Optional<List<RoleModel>> findByIdIn(List<String> ids);
    Optional<RoleModel> findByRole(String role);
}
