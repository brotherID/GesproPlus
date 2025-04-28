package com.management.pro.web.user;

import com.management.pro.dtos.user.CredentialDto;
import com.management.pro.dtos.user.UserAccountRequest;
import com.management.pro.dtos.user.UserAccountResponse;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;


@RequestMapping("/api/v1/users")
@CrossOrigin("*")
public interface UserAccountController {

    @PostMapping
    @PreAuthorize("@roleAccessHandler.hasPermission(T(com.management.pro.enums.PermissionEnum).USER_ADD)")
    ResponseEntity<Void> createUser(@RequestBody UserAccountRequest userAccountRequest);

    @GetMapping
    @PreAuthorize("@roleAccessHandler.hasPermission(T(com.management.pro.enums.PermissionEnum).USER_GET)")
    ResponseEntity<List<UserAccountResponse>> getAllUser();

    @GetMapping("/{idUserAccount}")
    @PreAuthorize("@roleAccessHandler.hasPermission(T(com.management.pro.enums.PermissionEnum).USER_GET)")
    ResponseEntity<UserAccountResponse> getUserById(@PathVariable String idUserAccount);

    @PutMapping("/{idUserAccount}")
    @PreAuthorize("@roleAccessHandler.hasPermission(T(com.management.pro.enums.PermissionEnum).USER_PUT)")
    ResponseEntity<UserAccountResponse> updateUser(@PathVariable String idUserAccount,@RequestBody UserAccountRequest userAccountRequest);

    @PutMapping("/reset/{idUserAccount}")
    @PreAuthorize("@roleAccessHandler.hasPermission(T(com.management.pro.enums.PermissionEnum).USER_PUT)")
    ResponseEntity<UserAccountResponse> resetPasswordUser(@PathVariable String idUserAccount,@RequestBody CredentialDto credentialDto);

    @DeleteMapping("/{idUserAccount}")
    @PreAuthorize("@roleAccessHandler.hasPermission(T(com.management.pro.enums.PermissionEnum).USER_DELETE)")
    ResponseEntity<Void> deleteRole(@PathVariable String idUserAccount);

}
