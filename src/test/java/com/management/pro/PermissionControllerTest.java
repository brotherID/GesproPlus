package com.management.pro;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.management.pro.config.RoleAccessHandler;
import com.management.pro.dtos.permission.PermissionRequest;
import com.management.pro.model.Permission;
import com.management.pro.repository.PermissionRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.context.annotation.Import;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

import static org.mockito.ArgumentMatchers.any;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@ActiveProfiles("test")
@AutoConfigureMockMvc
@Transactional
@Import(MockJwtUtils.class)
public class PermissionControllerTest {

    @Autowired
    private MockMvc mockMvc;


    @Autowired
    private PermissionRepository permissionRepository;


    @MockBean
    private RoleAccessHandler roleAccessHandler;

    @BeforeEach
    void setup() {
        Permission roleGet = new Permission("ROLE_GET", "ROLE_GET");
        Permission roleAdd = new Permission("ROLE_ADD", "ROLE_ADD");
        Permission rolePut = new Permission("ROLE_PUT", "ROLE_PUT");
        Permission roleDelete = new Permission("ROLE_DELETE", "ROLE_DELETE");
        Permission roleAddCandidate = new Permission("ADD_CANDIDATE", "ADD_CANDIDATE");
        Permission permissionAdd = new Permission("PERMISSION_ADD", "PERMISSION_ADD");
        permissionRepository.saveAll(List.of(roleGet, roleAdd,rolePut,roleDelete,roleAddCandidate,permissionAdd));
    }


    @Test
    void shouldReturnAllPermissions() throws Exception {
        Mockito.when(roleAccessHandler.hasPermission(any()))
                .thenReturn(true);

        mockMvc.perform(get("/api/v1/settings/permission")
                        .header(HttpHeaders.AUTHORIZATION, "Bearer " + MockJwtUtils.generateToken("superadmin", "superadmin")))
                .andExpect(status().isOk());
    }

    @Test
    void shouldGetPermissionById() throws Exception {
        Mockito.when(roleAccessHandler.hasPermission(any()))
                .thenReturn(true);

        mockMvc.perform(get("/api/v1/settings/permission/ROLE_GET")
                        .header(HttpHeaders.AUTHORIZATION, "Bearer "+ MockJwtUtils.generateToken("superadmin", "superadmin")))
                .andExpect(status().isOk());
    }

    @Test
    void shouldAddPermission() throws Exception {
        Mockito.when(roleAccessHandler.hasPermission(any()))
                .thenReturn(true);
        PermissionRequest permissionRequest = new PermissionRequest();
        permissionRequest.setPermission("PERMISSION_PUT");
        mockMvc.perform(get("/api/v1/settings/permission")
                        .header(HttpHeaders.AUTHORIZATION, "Bearer " + MockJwtUtils.generateToken("superadmin", "superadmin"))
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(new ObjectMapper().writeValueAsString(permissionRequest)))
                        .andExpect(status().isOk());
    }


    @Test
    void shouldUpdatePermission() throws Exception {
        Mockito.when(roleAccessHandler.hasPermission(any()))
                .thenReturn(true);
        PermissionRequest permissionRequest = new PermissionRequest();
        permissionRequest.setPermission("PERMISSION_DELETE");
        mockMvc.perform(put("/api/v1/settings/permission/PERMISSION_ADD")
                        .header(HttpHeaders.AUTHORIZATION, "Bearer " + MockJwtUtils.generateToken("superadmin", "superadmin"))
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(new ObjectMapper().writeValueAsString(permissionRequest)))
                .andExpect(status().isOk());
    }


    @Test
    void shouldDeleteRoleById() throws Exception {
        Mockito.when(roleAccessHandler.hasPermission(any())).thenReturn(true);

        mockMvc.perform(delete("/api/v1/settings/permission/PERMISSION_ADD")
                        .header(HttpHeaders.AUTHORIZATION, "Bearer "+ MockJwtUtils.generateToken("superadmin", "superadmin")))
                .andExpect(status().isNoContent());
    }

}
