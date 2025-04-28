package com.management.pro.service.user;

import com.management.pro.config.keycloak.KeycloakTokenProperties;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;
import org.springframework.web.client.RestTemplate;

import java.util.Map;

@Service
@RequiredArgsConstructor
public class KeycloakTokenService {

    private final KeycloakTokenProperties properties;

    public String getAdminToken() {
        RestTemplate restTemplate = new RestTemplate();
        MultiValueMap<String, String> form = new LinkedMultiValueMap<>();
        form.add("grant_type", properties.getGrantType());
        form.add("client_id", properties.getClientId());
        form.add("client_secret", properties.getClientSecret());
        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_FORM_URLENCODED);
        HttpEntity<?> entity = new HttpEntity<>(form, headers);
        ResponseEntity<Map> response = restTemplate.postForEntity(
                "http://localhost:8089/realms/bdcc_realm/protocol/openid-connect/token",
                entity,
                Map.class
        );
        return (String) response.getBody().get("access_token");
    }
}
