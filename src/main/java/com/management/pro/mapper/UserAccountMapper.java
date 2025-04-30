package com.management.pro.mapper;

import com.management.pro.dtos.user.UserAccountRequest;
import com.management.pro.dtos.user.UserAccountResponse;
import com.management.pro.model.UserAccount;
import org.mapstruct.*;

import java.util.List;

@Mapper(componentModel = "spring")
public interface UserAccountMapper {

    @Mapping(target = "idUserAccount", ignore = true)
    @Mapping(target = "roles", ignore = true)
    UserAccount toUserAccount(UserAccountRequest userAccountRequest);

    UserAccountResponse toUserAccountResponse(UserAccount userAccount);

    List<UserAccountResponse> toUserAccountResponses(List<UserAccount> userAccounts);

    @BeanMapping(nullValuePropertyMappingStrategy = NullValuePropertyMappingStrategy.IGNORE)
    void updateEntity(@MappingTarget UserAccount userAccount, UserAccountRequest userAccountRequest);
}
