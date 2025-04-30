package com.management.pro.repository;

import com.management.pro.model.Role;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;
import java.util.Set;

public interface RoleRepository extends JpaRepository<Role, String> {
    List<Role> findByIdIn(Set<String> ids);
    Optional<Role> findByRole(String role);
}
