package com.management.pro.mapper;

import com.management.pro.dtos.user.CredentialDto;
import com.management.pro.model.Credential;
import org.mapstruct.Mapper;

import java.util.List;


@Mapper(componentModel = "spring")
public interface CredentielMapper {
    CredentialDto toCredentialDto(Credential credential);

    Credential toCredential(CredentialDto credentialDto);


    List<CredentialDto> toCredentialDtos(List<Credential> credentials);

    List<Credential> toCredentials(List<CredentialDto> credentialDtos);

}
