package com.management.pro.config;

public interface RoleAccessHandler {

    Boolean hasPermission(String permission);

    Boolean hasRole(String role);
}
