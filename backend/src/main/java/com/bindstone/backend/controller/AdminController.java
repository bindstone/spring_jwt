package com.bindstone.backend.controller;

import com.bindstone.backend.dto.UserPayload;
import jakarta.ws.rs.core.Response;
import org.keycloak.admin.client.Keycloak;
import org.keycloak.admin.client.KeycloakBuilder;
import org.keycloak.admin.client.resource.RealmResource;
import org.keycloak.admin.client.resource.UsersResource;
import org.keycloak.representations.idm.CredentialRepresentation;
import org.keycloak.representations.idm.GroupRepresentation;
import org.keycloak.representations.idm.UserRepresentation;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import java.util.Collections;

@RestController
public class AdminController {

    @Value("${keycloak.server-url}")
    private String serverUrl;

    @Value("${keycloak.realm}")
    private String realm;

    @Value("${keycloak.admin.username}")
    private String adminUsername;

    @Value("${keycloak.admin.password}")
    private String adminPassword;

    @Value("${keycloak.admin.client-id}")
    private String adminClientId;

    @GetMapping("/admin")
    public ResponseEntity<String> hello() {
        return ResponseEntity.ok("Hello ADMIN");
    }

    @PostMapping("/admin/user")
    public ResponseEntity<String> createUser(@RequestBody UserPayload payload) {
        try (Keycloak keycloak = KeycloakBuilder.builder()
                .serverUrl(serverUrl)
                .realm("master")
                .username(adminUsername)
                .password(adminPassword)
                .clientId(adminClientId)
                .build()) {

            UserRepresentation user = new UserRepresentation();
            user.setEnabled(true);
            user.setUsername(payload.getUsername());
            user.setEmail(payload.getEmail());
            user.setFirstName(payload.getFirstName());
            user.setLastName(payload.getLastName());

            CredentialRepresentation credential = new CredentialRepresentation();
            credential.setType(CredentialRepresentation.PASSWORD);
            credential.setValue(payload.getPassword());
            credential.setTemporary(false);
            user.setCredentials(Collections.singletonList(credential));

            RealmResource realmResource = keycloak.realm(realm);
            UsersResource usersResource = realmResource.users();

            Response response = usersResource.create(user);

            if (response.getStatus() == 201) {
                String userId = response.getLocation().getPath().replaceAll(".*/([^/]+)$", "$1");

                //RoleRepresentation role = realmResource.roles().get("CONTINENTAL_ROLE_USER").toRepresentation();
                //realmResource.users().get(userId).roles().realmLevel().add(Collections.singletonList(role));

                //RoleRepresentation role = realmResource.clients().get("CONTINENTAL-CLIENT").roles().get("CONTINENTAL_ROLE_USER").toRepresentation();
                //realmResource.users().get(userId).roles().realmLevel().add(Collections.singletonList(role));
;
                GroupRepresentation group = realmResource.groups().groups().stream().filter(x -> x.getName().equals("CONTINENTAL_GROUP_USER")).findFirst().get();
                realmResource.users().get(userId).joinGroup(group.getId());

                return ResponseEntity.status(HttpStatus.CREATED).body("User created successfully");
            } else {
                return ResponseEntity.status(response.getStatus()).body("Failed to create user: " + response.getStatusInfo().getReasonPhrase());
            }
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("Error: " + e.getMessage());
        }
    }
}