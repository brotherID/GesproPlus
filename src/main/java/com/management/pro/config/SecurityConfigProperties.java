package com.management.pro.config;

import jakarta.validation.constraints.NotEmpty;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Configuration;
import org.springframework.validation.annotation.Validated;

@Configuration
@Validated
@ConfigurationProperties(prefix = "spring.security")
@Data
@NoArgsConstructor
@AllArgsConstructor
public class SecurityConfigProperties {
    private Boolean enabled;
    @NotEmpty
    private String allowedOrigins;
    @NotEmpty
    private String allowedMethods;
    @NotEmpty
    private String allowedHeaders;
    @NotEmpty
    private String exposedHeaders;

}
