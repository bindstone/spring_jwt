package com.bindstone.backend.config;

import org.springframework.boot.test.context.TestConfiguration;
import org.springframework.context.annotation.Bean;
import org.springframework.security.oauth2.jwt.JwtDecoder;
import org.springframework.security.oauth2.jwt.NimbusJwtDecoder;

import javax.crypto.SecretKey;
import javax.crypto.spec.SecretKeySpec;

@TestConfiguration
public class TestSecurityConfig {

    private static final String SECRET_KEY = "mySecretKey123456789012345678901234567890";
    private static final SecretKey key = new SecretKeySpec(SECRET_KEY.getBytes(), "HMACSHA256");

    @Bean
    public JwtDecoder jwtDecoder() {
        return NimbusJwtDecoder.withSecretKey(key).build();
    }

    public static SecretKey getTestKey() {
        return key;
    }
}
