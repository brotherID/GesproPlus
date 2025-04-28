package com.management.pro.utils;

import lombok.Getter;
import lombok.Setter;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.PropertySource;
import org.springframework.stereotype.Component;

@Component
@PropertySource(value = "classpath:messages.yml", factory = YamlPropertySourceFactory.class)
@ConfigurationProperties(prefix = "app.messages")
@Getter
@Setter
public class AppMessagesProperties {
    private String roleAlreadyExists;
    private String permissionsDoNotExist;
    private String roleNotFound;
    private String permissionNotFoundInRole;
    private String permissionAlreadyExistsInRole;
    private String permissionNotFound;
    private String permissionNotFoundWithId;
    private String permissionAlreadyExists;
    private String userExistsWithSameEmail;
    private String failedToCreateUserInKeycloak;
    private String userNotFound;
    private String failedToDeleteUserInKeycloak;
    private String failedToUpdatePasswordUserInKeycloak;
}
