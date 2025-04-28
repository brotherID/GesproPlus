package com.management.pro;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.management.pro.config.RoleAccessHandler;
import com.management.pro.dtos.role.RoleRequest;
import com.management.pro.dtos.user.UserAccountRequest;
import com.management.pro.model.Permission;
import com.management.pro.model.Role;
import com.management.pro.model.UserAccount;
import com.management.pro.repository.PermissionRepository;
import com.management.pro.repository.RoleRepository;
import com.management.pro.repository.UserAccountRepository;
import com.management.pro.service.user.KeycloakTokenService;
import com.management.pro.service.user.UserAccountServiceImpl;
import lombok.extern.slf4j.Slf4j;
import org.apache.http.HttpEntity;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.context.annotation.Import;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.client.RestTemplate;

import java.util.List;

import static org.mockito.ArgumentMatchers.any;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@ActiveProfiles("test")
@AutoConfigureMockMvc
@Transactional
@Import(MockJwtUtils.class)
@Slf4j
public class UserAccountControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private RoleRepository roleRepository;

    @Autowired
    private PermissionRepository permissionRepository;

    @Autowired
    private UserAccountRepository userAccountRepository;

    @MockBean
    private KeycloakTokenService keycloakTokenService;

    @Mock
    private RestTemplate restTemplate;



    @MockBean
    private RoleAccessHandler roleAccessHandler;

    @BeforeEach
    void setup() {
        Permission roleGet = new Permission("USER_GET", "USER_GET");
        Permission roleAdd = new Permission("USER_ADD", "USER_ADD");
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

        UserAccount userAccount = new UserAccount();
        userAccount.setIdUserAccount("f60e235b-9354-451a-a18b-fdccb81a8f88");
        userAccount.setUsername("user1");
        userAccount.setRole(role);
        userAccountRepository.save(userAccount);

    }

    @Test
    void shouldReturnAllUsers() throws Exception {
        Mockito.when(roleAccessHandler.hasPermission(any()))
                .thenReturn(true);

        mockMvc.perform(get("/api/v1/users")
                        .header(HttpHeaders.AUTHORIZATION, "Bearer " + MockJwtUtils.generateToken("superadmin", "superadmin")))
                .andExpect(status().isOk());
    }

    @Test
    void shouldReturnUserById() throws Exception {
        Mockito.when(roleAccessHandler.hasPermission(any()))
                .thenReturn(true);

        mockMvc.perform(get("/api/v1/users/f60e235b-9354-451a-a18b-fdccb81a8f88")
                        .header(HttpHeaders.AUTHORIZATION, "Bearer "+ MockJwtUtils.generateToken("superadmin", "superadmin")))
                .andExpect(status().isOk());
    }

    @Test
    void shouldCreateUser() throws Exception {
        Mockito.when(roleAccessHandler.hasPermission(any()))
                .thenReturn(true);
        Mockito.when(keycloakTokenService.getAdminToken())
                .thenReturn("fake-token");
        Mockito.when(restTemplate.postForEntity(
                Mockito.anyString(),
                Mockito.any(HttpEntity.class),
                Mockito.eq(Void.class)
        )).thenReturn(new ResponseEntity<>(HttpStatus.CREATED));
        UserAccountRequest userAccountRequest = new UserAccountRequest();
        userAccountRequest.setUsername("user2");
        userAccountRequest.setEmail("user2@gmail.com");
        userAccountRequest.setRoleId("superadmin");
        mockMvc.perform(post("/api/v1/users")
                        .header(HttpHeaders.AUTHORIZATION, "Bearer " + MockJwtUtils.generateToken("superadmin", "superadmin"))
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(new ObjectMapper().writeValueAsString(userAccountRequest)))
                .andExpect(status().isCreated());
    }
}
