package com.management.pro.service.permission;

import com.management.pro.dtos.permission.PermissionRequest;
import com.management.pro.dtos.permission.PermissionResponse;
import com.management.pro.exceptions.ConflictException;
import com.management.pro.mapper.PermissionMapper;
import com.management.pro.model.Permission;
import com.management.pro.model.Role;
import com.management.pro.repository.PermissionRepository;
import com.management.pro.repository.RoleRepository;
import com.management.pro.utils.AppMessagesProperties;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
@Slf4j
public class PermissionServiceImpl implements PermissionService {

    private final PermissionRepository permissionRepository;
    private final PermissionMapper permissionMapper;
    private final RoleRepository roleRepository;
    private final AppMessagesProperties appMessagesProperties;

    @Override
    public List<Permission> getAllPermissions() {
        return permissionRepository.findAll();
    }

    private Permission findPermissionOrFail(String idPermission) {
        return permissionRepository.findById(idPermission)
                .orElseThrow(() -> new ConflictException(appMessagesProperties.getPermissionNotFoundWithId()   + idPermission));
    }

    @Override
    public PermissionResponse getPermissionById(String idPermission) {
         log.info("begin getPermissionById : {}" , idPermission);
         Permission permission = findPermissionOrFail(idPermission) ;
         return  permissionMapper.toPermissionDto(permission);
    }

    @Override
    public PermissionResponse createPermission(PermissionRequest permissionRequest) {
        log.info("Begin createPermission : {}",  permissionRequest);
        permissionRepository.findById(permissionRequest.getPermission())
                .ifPresent(permissionFounded -> {
                    throw new ConflictException(appMessagesProperties.getPermissionAlreadyExists());
                });
        Permission permission =  permissionMapper.toPermission(permissionRequest);
        return permissionMapper.toPermissionDto(permissionRepository.save(permission));
    }

    @Override
    public PermissionResponse updatePermission(String idPermission ,PermissionRequest permissionRequest) {
        log.info("Begin updatePermission : {} , {}",idPermission ,permissionRequest);
        Permission existingPermission = permissionRepository.findById(idPermission)
                .orElseThrow(() -> new ConflictException(appMessagesProperties.getPermissionNotFoundWithId()  + " " + idPermission));

        if(permissionRequest.getPermission() != null) {
           existingPermission.setPermission(permissionRequest.getPermission());
        }
        Permission updatedPermission =  permissionRepository.save(existingPermission);
        log.info("updatedPermission : {}", updatedPermission);
        return permissionMapper.toPermissionDto(updatedPermission);
    }

    @Override
    public void deletePermission(String id) {
        log.info("Begin deletePermission : {}", id);
        Permission permission = findPermissionOrFail(id);
        List<Role> rolesToUpdate = roleRepository.findAll().stream()
                .peek(role -> role.getPermissions().removeIf(p -> p.getId().equals(id)))
                .collect(Collectors.toList());
        roleRepository.saveAll(rolesToUpdate);
        permissionRepository.delete(permission);
    }
}
