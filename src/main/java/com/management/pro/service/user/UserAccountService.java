package com.management.pro.service.user;

import com.management.pro.dtos.user.CredentialDto;
import com.management.pro.dtos.user.UserAccountRequest;
import com.management.pro.dtos.user.UserAccountResponse;

import java.util.List;

public interface UserAccountService {
    void createUser(UserAccountRequest userAccountRequest);
    List<UserAccountResponse> getAllUsers();
    UserAccountResponse getUserById(String idUserAccount);
    UserAccountResponse updateUser(String idUserAccount, UserAccountRequest userAccountRequest);
    void deleteUser(String idUserAccount);
    UserAccountResponse resetPassword(String idUserAccount, CredentialDto credentialDto);
}
