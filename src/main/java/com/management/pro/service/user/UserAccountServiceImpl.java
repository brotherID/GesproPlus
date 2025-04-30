package com.management.pro.service.user;

import com.management.pro.dtos.user.CredentialDto;
import com.management.pro.dtos.user.UserAccountRequest;
import com.management.pro.dtos.user.UserAccountResponse;
import com.management.pro.dtos.user.UserKeycloak;
import com.management.pro.exceptions.ConflictException;
import com.management.pro.mapper.UserAccountMapper;
import com.management.pro.model.Role;
import com.management.pro.model.UserAccount;
import com.management.pro.repository.RoleRepository;
import com.management.pro.repository.UserAccountRepository;
import com.management.pro.utils.AppMessagesProperties;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.*;
import org.springframework.stereotype.Service;
import org.springframework.util.CollectionUtils;
import org.springframework.web.client.RestTemplate;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

@Service
@RequiredArgsConstructor
@Slf4j
public class UserAccountServiceImpl implements UserAccountService {

    private final UserAccountRepository userAccountRepository;
    private final RoleRepository roleRepository;
    private final RestTemplate restTemplate;
    private final UserAccountMapper userAccountMapper;
    private final KeycloakTokenService keycloakTokenService;
    private final AppMessagesProperties appMessagesProperties;

    @Override
    public void createUser(UserAccountRequest userAccountRequest) {
        log.info("Create user account : {}", userAccountRequest);
        checkExistingUserAccount(userAccountRequest);
        UserKeycloak userKeycloak = getUserKeycloak(userAccountRequest);
        HttpHeaders headers = new HttpHeaders();
        headers.setBearerAuth(keycloakTokenService.getAdminToken());
        headers.setContentType(MediaType.APPLICATION_JSON);
        HttpEntity<UserKeycloak> entity = new HttpEntity<>(userKeycloak, headers);
        ResponseEntity<Void> response = restTemplate.postForEntity(
                "http://localhost:8089/admin/realms/bdcc_realm/users",
                entity,
                Void.class
        );
        if (response.getStatusCode() != HttpStatus.CREATED) {
            throw new ConflictException(appMessagesProperties.getFailedToCreateUserInKeycloak() + response.getBody());
        }
        String location = response.getHeaders().getLocation().toString();
        String userId = location.substring(location.lastIndexOf('/') + 1);
        UserAccount user = userAccountMapper.toUserAccount(userAccountRequest);
        user.setIdUserAccount(userId);
        user.setRoles(findRolesIfExist(userAccountRequest.getRolesName()));
        userAccountRepository.save(user);
    }



    private List<Role> findRolesIfExist(List<String> roles) {
        log.info("findRolesIfExist [roles: {}] ...", roles);
        if (!CollectionUtils.isEmpty(roles)) {
            Set<String> roleSet = new HashSet<>(roles);
            List<Role> persistedRoles = roleRepository.findByIdIn(roleSet);
            if (persistedRoles.size() != roleSet.size()) {
                throw new ConflictException("Some roles do not exist");
            }
            return persistedRoles;
        }
        log.info("findRolesIfExist [roles: {}] Done", roles);
        return null;
    }




    @Override
    public List<UserAccountResponse> getAllUsers() {
        return userAccountMapper.toUserAccountResponses(userAccountRepository.findAll());
    }

    @Override
    public UserAccountResponse getUserById(String idUserAccount) {
        UserAccount userAccount = userAccountRepository.findById(idUserAccount).orElseThrow(()-> new ConflictException(appMessagesProperties.getUserNotFound()));
        return userAccountMapper.toUserAccountResponse(userAccount);
    }

    @Override
    public UserAccountResponse updateUser(String idUserAccount, UserAccountRequest userAccountRequest) {
        UserAccount user = userAccountRepository.findById(idUserAccount)
                .orElseThrow(() -> new ConflictException(appMessagesProperties.getUserNotFound()));
        UserKeycloak userKeycloak = getUserKeycloak(userAccountRequest);
        String token = keycloakTokenService.getAdminToken();
        HttpHeaders headers = new HttpHeaders();
        headers.setBearerAuth(token);
        headers.setContentType(MediaType.APPLICATION_JSON);
        HttpEntity<UserKeycloak> entity = new HttpEntity<>(userKeycloak, headers);
        restTemplate.exchange(
                "http://localhost:8089/admin/realms/bdcc_realm/users/" + idUserAccount,
                HttpMethod.PUT,
                entity,
                Void.class
        );
        userAccountMapper.updateEntity(user, userAccountRequest);
        return userAccountMapper.toUserAccountResponse(userAccountRepository.save(user));
    }



    @Override
    public void deleteUser(String idUserAccount) {
        userAccountRepository.findById(idUserAccount)
                .orElseThrow(() -> new ConflictException(appMessagesProperties.getUserNotFound()));
        String token = keycloakTokenService.getAdminToken();
        HttpHeaders headers = new HttpHeaders();
        headers.setBearerAuth(token);
        HttpEntity<Void> entity = new HttpEntity<>(headers);
        ResponseEntity<Void> response = restTemplate.exchange(
                "http://localhost:8089/admin/realms/bdcc_realm/users/" + idUserAccount,
                HttpMethod.DELETE,
                entity,
                Void.class
        );
        if (!response.getStatusCode().is2xxSuccessful()) {
            throw new ConflictException(appMessagesProperties.getFailedToDeleteUserInKeycloak());
        }
        userAccountRepository.deleteById(idUserAccount);
    }

    @Override
    public UserAccountResponse resetPassword(String idUserAccount, CredentialDto credentialDto) {
        log.info("Begin resetPassword : idUserAccount {} , {}", idUserAccount, credentialDto);
        UserAccount user = userAccountRepository.findById(idUserAccount)
                .orElseThrow(() -> new ConflictException(appMessagesProperties.getUserNotFound()));
        HttpHeaders headers = new HttpHeaders();
        headers.setBearerAuth(keycloakTokenService.getAdminToken());
        HttpEntity<CredentialDto> entity = new HttpEntity<>(credentialDto,headers);
        ResponseEntity<Void> response = restTemplate.exchange(
                "http://localhost:8089/admin/realms/bdcc_realm/users/" + idUserAccount + "/reset-password",
                HttpMethod.PUT,
                entity,
                Void.class
        );
        if (!response.getStatusCode().is2xxSuccessful()) {
            throw new ConflictException(appMessagesProperties.getFailedToUpdatePasswordUserInKeycloak());
        }
        user.setPassword(credentialDto.getValue());
        return  userAccountMapper.toUserAccountResponse(userAccountRepository.save(user));

    }

    private void checkExistingUserAccount(UserAccountRequest userAccountRequest) {
        log.info("checkExistingUserAccount : {}", userAccountRequest);
        userAccountRepository.findByEmail(userAccountRequest.getEmail())
                        .ifPresent(existing -> {
                            throw new ConflictException(appMessagesProperties.getUserExistsWithSameEmail());
                        });
    }

    private static UserKeycloak getUserKeycloak(UserAccountRequest userAccountRequest) {
        UserKeycloak userKeycloak = new UserKeycloak();
        userKeycloak.setUsername(userAccountRequest.getUsername());
        userKeycloak.setEnabled(userAccountRequest.isEnabled());
        userKeycloak.setFirstName(userAccountRequest.getFirstName());
        userKeycloak.setLastName(userAccountRequest.getLastName());
        userKeycloak.setEmail(userAccountRequest.getEmail());
        userKeycloak.setEmailVerified(userAccountRequest.isEmailVerified());
        CredentialDto credentialDto = new CredentialDto();
        credentialDto.setTemporary(true);
        credentialDto.setValue(userAccountRequest.getPassword());
        credentialDto.setType("password");
        List<CredentialDto>  credentialDtos = new ArrayList<>();
        credentialDtos.add(credentialDto);
        userKeycloak.setCredentials(credentialDtos);
        return userKeycloak;
    }


}
