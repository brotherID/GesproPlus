package com.management.pro.web.user;

import com.management.pro.dtos.user.CredentialDto;
import com.management.pro.dtos.user.UserAccountRequest;
import com.management.pro.dtos.user.UserAccountResponse;
import com.management.pro.service.user.UserAccountService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequiredArgsConstructor
@Slf4j
public class UserAccountControllerImpl implements  UserAccountController{

    private final UserAccountService userAccountService;

    @Override
    public ResponseEntity<Void> createUser(UserAccountRequest userAccountRequest) {
        log.info("createUser : {}", userAccountRequest);
        userAccountService.createUser(userAccountRequest);
        return ResponseEntity.status(HttpStatus.CREATED).build();
    }

    @Override
    public ResponseEntity<List<UserAccountResponse>> getAllUser() {
        return ResponseEntity.ok(userAccountService.getAllUsers());
    }

    @Override
    public ResponseEntity<UserAccountResponse> getUserById(String idUserAccount) {
        return ResponseEntity.ok(userAccountService.getUserById(idUserAccount));
    }

    @Override
    public ResponseEntity<UserAccountResponse> updateUser(String idUserAccount, UserAccountRequest userAccountRequest) {
        return ResponseEntity.ok(userAccountService.updateUser(idUserAccount, userAccountRequest));
    }

    @Override
    public ResponseEntity<UserAccountResponse> resetPasswordUser(String idUserAccount, CredentialDto credentialDto) {
        return ResponseEntity.ok(userAccountService.resetPassword(idUserAccount, credentialDto));
    }

    @Override
    public ResponseEntity<Void> deleteRole(String idUserAccount) {
        userAccountService.deleteUser(idUserAccount);
        return ResponseEntity.noContent().build();
    }
}
