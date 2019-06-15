package com.demo.account.config;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Configuration;

@Configuration
@ConfigurationProperties(value = "vault")
@EnableConfigurationProperties(VaultSecret.class)
@Data
@AllArgsConstructor
@NoArgsConstructor
public class VaultSecret {
    private String username;
    private String password;
}
