package com.management.pro.config;

import lombok.NoArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.core.context.SecurityContext;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.oauth2.core.OAuth2TokenIntrospectionClaimNames;
import org.springframework.security.oauth2.jwt.Jwt;
import org.springframework.security.oauth2.server.resource.authentication.JwtAuthenticationToken;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;

import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.Optional;
import java.util.stream.Stream;

@Slf4j
@NoArgsConstructor
public class SecurityContextHandler {
    public static final String USER_HEADER = "X-USER";

    public static Jwt getJwt() {
        return Optional.ofNullable(SecurityContextHolder.getContext())
                .map(SecurityContext::getAuthentication)
                .filter(authentication -> authentication instanceof JwtAuthenticationToken || authentication instanceof Jwt)
                .map(authentication -> authentication instanceof JwtAuthenticationToken jwtAuthenticationToken ? jwtAuthenticationToken.getToken() : ((Jwt) authentication))
                .orElse(null);
    }

    public static List<String> getRoles() {
        return Optional.ofNullable(getJwt())
                .map(jwt -> jwt.getClaim("realm_access"))
                .filter(Map.class::isInstance)
                .map(Map.class::cast)
                .map(realmAccess -> (List<?>) realmAccess.get("roles"))
                .map(roles -> roles.stream()
                        .filter(String.class::isInstance)
                        .map(String.class::cast)
                        .toList())
                .orElse(List.of());
    }

    public static String getConnectedUser() {
        return Optional.ofNullable(getJwt())
                .map(SecurityContextHandler::extractConnectedUserIdentifier)
                .orElse(null);
    }

    public static String extractConnectedUserIdentifier(Jwt token) {
        return Stream.of(
                        SecurityContextHandler.getOriginalUser(),
                        token.getClaimAsString("preferred_username"),
                        token.getClaimAsString(OAuth2TokenIntrospectionClaimNames.CLIENT_ID),
                        token.getClaimAsString("azp"),
                        token.getClaimAsString("email")
                )
                .filter(Objects::nonNull)
                .findFirst()
                .orElse(null);
    }

    public static String getOriginalUser() {
        return Optional.ofNullable(RequestContextHolder.getRequestAttributes())
                .filter(ServletRequestAttributes.class::isInstance)
                .map(ServletRequestAttributes.class::cast)
                .map(ServletRequestAttributes::getRequest)
                .map(e -> e.getHeader(USER_HEADER))
                .orElseGet(() -> {
                    log.warn("[{}] header not found! returning null...", USER_HEADER);
                    return null;
                });
    }

}
