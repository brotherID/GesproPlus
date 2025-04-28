package com.management.pro.config.keycloak;

import lombok.Getter;
import lombok.Setter;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;

@Component
@ConfigurationProperties(prefix = "keycloak.token")
@Getter
@Setter
public class KeycloakTokenProperties {
    private String grantType;
    private String clientId;
    private String clientSecret;
}
