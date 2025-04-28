package com.management.pro.repository;

import com.management.pro.model.Role;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface RoleRepository extends JpaRepository<Role, String> {
    List<Role> findByIdIn(List<String> ids);
    Optional<Role> findByRole(String role);
}
