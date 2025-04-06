package com.management.pro.config;

import io.micrometer.tracing.Tracer;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.security.config.Customizer;
import org.springframework.security.config.annotation.method.configuration.EnableMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;
import org.springframework.security.config.annotation.web.configurers.HeadersConfigurer;
import org.springframework.security.config.annotation.web.configurers.RequestCacheConfigurer;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.header.writers.XXssProtectionHeaderWriter;
import org.springframework.web.cors.CorsConfiguration;
import org.springframework.web.servlet.handler.MappedInterceptor;

import java.util.List;

@Configuration
@EnableWebSecurity
@EnableMethodSecurity
@Slf4j
@RequiredArgsConstructor
@ConditionalOnProperty(name = "security.enabled", havingValue = "true", matchIfMissing = true)
public class SecurityConfiguration {
    protected static final String[] AUTH_WHITELIST = {"/v3/api-docs/**", "/swagger*/**", "/webjars/**", "/actuator/**", "/api-docs/**"};
    private static final String COMMA = ",";
    private final SecurityConfigProperties springSecurityProperties;
    @Bean
    public SecurityFilterChain configure(HttpSecurity http) throws Exception {
        log.info("Configuring Web Security...");

        http.cors(cors -> cors.configurationSource(request -> {
            final var corsConfiguration = new CorsConfiguration();
            corsConfiguration.setAllowedOrigins(List.of(springSecurityProperties.getAllowedOrigins().split(COMMA)));
            corsConfiguration.setAllowedMethods(List.of(springSecurityProperties.getAllowedMethods().split(COMMA)));
            corsConfiguration.setAllowedHeaders(List.of(springSecurityProperties.getAllowedHeaders().split(COMMA)));
            corsConfiguration.setExposedHeaders(List.of(springSecurityProperties.getExposedHeaders().split(COMMA)));
            return corsConfiguration;
        }));

        http.headers(httpSecurityHeadersConfigurer ->
                httpSecurityHeadersConfigurer
                        .xssProtection(xss -> xss.headerValue(XXssProtectionHeaderWriter.HeaderValue.ENABLED_MODE_BLOCK))
                        .frameOptions(HeadersConfigurer.FrameOptionsConfig::sameOrigin)
        );

        http.sessionManagement(AbstractHttpConfigurer::disable);

        http.requestCache(RequestCacheConfigurer::disable);

        http.csrf(AbstractHttpConfigurer::disable);

        http.authorizeHttpRequests(accessManagement -> accessManagement
                .requestMatchers(HttpMethod.OPTIONS).permitAll()
                .requestMatchers(AUTH_WHITELIST).permitAll()
                .anyRequest().authenticated()
        );

        http.oauth2ResourceServer(oauth2 -> oauth2.jwt(Customizer.withDefaults()));

        return http.build();
    }

    @Bean
    public MappedInterceptor authorityHandler(Tracer tracer) {
        return new MappedInterceptor(null, new AuthorityInterceptor(tracer));
    }




}
