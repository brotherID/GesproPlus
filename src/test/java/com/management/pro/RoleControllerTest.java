package com.management.pro;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.management.pro.config.RoleAccessHandler;
import com.management.pro.dtos.role.RoleRequest;
import com.management.pro.model.Permission;
import com.management.pro.model.Role;
import com.management.pro.repository.PermissionRepository;
import com.management.pro.repository.RoleRepository;
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
public class RoleControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private RoleRepository roleRepository;

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
        Permission roleADDCANDIDATE = new Permission("ADD_CANDIDATE", "ADD_CANDIDATE");
        permissionRepository.saveAll(List.of(roleGet, roleAdd,rolePut,roleDelete,roleADDCANDIDATE));

        Role role = new Role();
        role.setId("SUPERADMIN");
        role.setRole("SUPERADMIN");
        role.setIsAdmin(true);
        role.setIsSuperAdmin(true);
        role.setPermissions(List.of(roleGet, roleAdd,rolePut,roleDelete));
        roleRepository.save(role);
    }


    @Test
    void shouldReturnRolesWhenPermissionGranted() throws Exception {
        Mockito.when(roleAccessHandler.hasPermission(any()))
                .thenReturn(true);

        mockMvc.perform(get("/api/v1/settings/role")
                        .header(HttpHeaders.AUTHORIZATION, "Bearer " + MockJwtUtils.generateToken("superadmin", "superadmin")))
                .andExpect(status().isOk());
    }


    @Test
    void shouldReturnRoleById_whenPermissionIsGranted() throws Exception {
        Mockito.when(roleAccessHandler.hasPermission(any()))
                .thenReturn(true);

        mockMvc.perform(get("/api/v1/settings/role/SUPERADMIN")
                        .header(HttpHeaders.AUTHORIZATION, "Bearer "+ MockJwtUtils.generateToken("superadmin", "superadmin")))
                .andExpect(status().isOk());
    }


    @Test
    void shouldCreateRole_whenPermissionIsGranted() throws Exception {
        Mockito.when(roleAccessHandler.hasPermission(any()))
                .thenReturn(true);
        RoleRequest roleRequest = new RoleRequest();
        roleRequest.setRole("TEST_ROLE");
        mockMvc.perform(post("/api/v1/settings/role")
                        .header(HttpHeaders.AUTHORIZATION, "Bearer " + MockJwtUtils.generateToken("superadmin", "superadmin"))
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(new ObjectMapper().writeValueAsString(roleRequest)))
                .andExpect(status().isOk());
    }


    @Test
    void shouldUpdateRole_whenPermissionIsGranted() throws Exception {
        Mockito.when(roleAccessHandler.hasPermission(any()))
                .thenReturn(true);
        RoleRequest roleRequest = new RoleRequest();
        roleRequest.setRole("SUPERADMIN");
        mockMvc.perform(put("/api/v1/settings/role")
                        .header(HttpHeaders.AUTHORIZATION, "Bearer " + MockJwtUtils.generateToken("superadmin", "superadmin"))
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(new ObjectMapper().writeValueAsString(roleRequest)))
                .andExpect(status().isOk());
    }



    @Test
    void shouldAddPermissionToRole() throws Exception {
        Mockito.when(roleAccessHandler.hasPermission(any())).thenReturn(true);

        mockMvc.perform(put("/api/v1/settings/role/SUPERADMIN/permissions/ADD_CANDIDATE")
                        .header(HttpHeaders.AUTHORIZATION, "Bearer "+ MockJwtUtils.generateToken("superadmin", "superadmin")))
                .andExpect(status().isOk());
    }

    @Test
    void shouldRemovePermissionFromRole() throws Exception {
        Mockito.when(roleAccessHandler.hasPermission(any())).thenReturn(true);

        mockMvc.perform(delete("/api/v1/settings/role/SUPERADMIN/permissions/ROLE_PUT")
                        .header(HttpHeaders.AUTHORIZATION, "Bearer "+ MockJwtUtils.generateToken("superadmin", "superadmin")))
                .andExpect(status().isOk());
    }

    @Test
    void shouldDeleteRoleById() throws Exception {
        Mockito.when(roleAccessHandler.hasPermission(any())).thenReturn(true);

        mockMvc.perform(delete("/api/v1/settings/role/ADMIN")
                        .header(HttpHeaders.AUTHORIZATION, "Bearer "+ MockJwtUtils.generateToken("superadmin", "superadmin")))
                .andExpect(status().isNoContent());
    }

    @Test
    void shouldReturnCurrentRole() throws Exception {
        mockMvc.perform(get("/api/v1/settings/role/current")
                        .header(HttpHeaders.AUTHORIZATION, "Bearer "+ MockJwtUtils.generateToken("superadmin", "superadmin")))
                .andExpect(status().isOk());
    }
}
