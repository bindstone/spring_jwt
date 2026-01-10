package com.bindstone.backend.utils;

import com.bindstone.backend.config.TestSecurityConfig;
import io.jsonwebtoken.Jwts;

import javax.crypto.SecretKey;
import java.time.Instant;
import java.time.temporal.ChronoUnit;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class JwtTestUtils {

    private static final SecretKey SECRET_KEY = TestSecurityConfig.getTestKey();
    
    public static String generateValidToken() {
        Instant now = Instant.now();
        return Jwts.builder()
                .setSubject("test-user")
                .setIssuedAt(Date.from(now))
                .setExpiration(Date.from(now.plus(1, ChronoUnit.HOURS)))
                .claim("realm_access", Map.of("roles", List.of("USER")))
                .signWith(SECRET_KEY)
                .compact();
    }
    
    public static String generateUserToken() {
        Instant now = Instant.now();
        return Jwts.builder()
                .setSubject("user")
                .setIssuedAt(Date.from(now))
                .setExpiration(Date.from(now.plus(1, ChronoUnit.HOURS)))
                .claim("realm_access", Map.of("roles", List.of("USER")))
                .signWith(SECRET_KEY)
                .compact();
    }
    
    public static String generateAdminToken() {
        Instant now = Instant.now();
        return Jwts.builder()
                .setSubject("admin")
                .setIssuedAt(Date.from(now))
                .setExpiration(Date.from(now.plus(1, ChronoUnit.HOURS)))
                .claim("realm_access", Map.of("roles", List.of("CONTINENTAL_ROLE_ADMIN")))
                .signWith(SECRET_KEY)
                .compact();
    }
    
    public static String generateMultiRoleToken() {
        Instant now = Instant.now();
        return Jwts.builder()
                .setSubject("multi-role-user")
                .setIssuedAt(Date.from(now))
                .setExpiration(Date.from(now.plus(1, ChronoUnit.HOURS)))
                .claim("realm_access", Map.of("roles", List.of("USER", "CONTINENTAL_ROLE_ADMIN", "MANAGER")))
                .signWith(SECRET_KEY)
                .compact();
    }
    
    public static String generateResourceAdminToken() {
        Instant now = Instant.now();
        Map<String, Object> resourceAccess = new HashMap<>();
        resourceAccess.put("backend-app", Map.of("roles", List.of("CONTINENTAL_ROLE_ADMIN")));
        
        return Jwts.builder()
                .setSubject("resource-admin")
                .setIssuedAt(Date.from(now))
                .setExpiration(Date.from(now.plus(1, ChronoUnit.HOURS)))
                .claim("resource_access", resourceAccess)
                .signWith(SECRET_KEY)
                .compact();
    }
    
    public static String generateExpiredToken() {
        Instant past = Instant.now().minus(1, ChronoUnit.HOURS);
        return Jwts.builder()
                .setSubject("expired-user")
                .setIssuedAt(Date.from(past))
                .setExpiration(Date.from(past.plus(30, ChronoUnit.MINUTES)))
                .claim("realm_access", Map.of("roles", List.of("USER")))
                .signWith(SECRET_KEY)
                .compact();
    }
    
    public static String generateTokenWithoutRoles() {
        Instant now = Instant.now();
        return Jwts.builder()
                .setSubject("no-roles-user")
                .setIssuedAt(Date.from(now))
                .setExpiration(Date.from(now.plus(1, ChronoUnit.HOURS)))
                .signWith(SECRET_KEY)
                .compact();
    }
    
    public static String generateTokenWithCustomClaims(Map<String, Object> claims) {
        Instant now = Instant.now();
        var builder = Jwts.builder()
                .setSubject("custom-user")
                .setIssuedAt(Date.from(now))
                .setExpiration(Date.from(now.plus(1, ChronoUnit.HOURS)))
                .signWith(SECRET_KEY);
        
        claims.forEach(builder::claim);
        return builder.compact();
    }
    
    public static SecretKey getSecretKey() {
        return SECRET_KEY;
    }
}
