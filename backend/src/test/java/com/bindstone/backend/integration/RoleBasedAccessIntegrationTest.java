package com.bindstone.backend.integration;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureWebMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.web.context.WebApplicationContext;

import static org.springframework.security.test.web.servlet.setup.SecurityMockMvcConfigurers.springSecurity;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@SpringBootTest
@AutoConfigureWebMvc
class RoleBasedAccessIntegrationTest {

    @Autowired
    private WebApplicationContext webApplicationContext;

    private MockMvc mockMvc;

    @BeforeEach
    void setUp() {
        this.mockMvc = MockMvcBuilders.webAppContextSetup(this.webApplicationContext)
                .apply(springSecurity())
                .build();
    }

    @Test
    void testPublicEndpointAccessForAll() throws Exception {
        mockMvc.perform(get("/public"))
                .andExpect(status().isOk())
                .andExpect(content().string("Hello PUBLIC"));
    }

    @Test
    @WithMockUser
    void testPublicEndpointAccessWithAuthenticatedUser() throws Exception {
        mockMvc.perform(get("/public"))
                .andExpect(status().isOk())
                .andExpect(content().string("Hello PUBLIC"));
    }

    @Test
    @WithMockUser(roles = {"CONTINENTAL_ROLE_ADMIN"})
    void testPublicEndpointAccessWithAdminUser() throws Exception {
        mockMvc.perform(get("/public"))
                .andExpect(status().isOk())
                .andExpect(content().string("Hello PUBLIC"));
    }

    @Test
    void testPrivateEndpointAccessForAuthenticatedUsers() throws Exception {
        mockMvc.perform(get("/private"))
                .andExpect(status().isUnauthorized());
    }

    @Test
    @WithMockUser
    void testPrivateEndpointAccessWithAuthenticatedUser() throws Exception {
        mockMvc.perform(get("/private"))
                .andExpect(status().isOk())
                .andExpect(content().string("Hello PRIVATE"));
    }

    @Test
    @WithMockUser(roles = {"CONTINENTAL_ROLE_ADMIN"})
    void testPrivateEndpointAccessWithAdminUser() throws Exception {
        mockMvc.perform(get("/private"))
                .andExpect(status().isOk())
                .andExpect(content().string("Hello PRIVATE"));
    }

    @Test
    void testAdminEndpointAccessForAdminUsers() throws Exception {
        mockMvc.perform(get("/admin"))
                .andExpect(status().isUnauthorized());
    }

    @Test
    @WithMockUser
    void testAdminEndpointAccessWithAuthenticatedUser() throws Exception {
        mockMvc.perform(get("/admin"))
                .andExpect(status().isForbidden());
    }

    @Test
    @WithMockUser(roles = {"CONTINENTAL_ROLE_ADMIN"})
    void testAdminEndpointAccessWithAdminUser() throws Exception {
        mockMvc.perform(get("/admin"))
                .andExpect(status().isOk())
                .andExpect(content().string("Hello ADMIN"));
    }

    @Test
    @WithMockUser(roles = {"USER"})
    void testRoleHierarchy() throws Exception {
        mockMvc.perform(get("/admin"))
                .andExpect(status().isForbidden());
        
        mockMvc.perform(get("/private"))
                .andExpect(status().isOk());
    }

    @Test
    @WithMockUser(roles = {"CONTINENTAL_ROLE_ADMIN"})
    void testCrossOriginRequests() throws Exception {
        mockMvc.perform(get("/public")
                .header("Origin", "http://localhost:3000"))
                .andExpect(status().isOk());
        
        mockMvc.perform(get("/private")
                .header("Origin", "http://localhost:3000"))
                .andExpect(status().isOk());
    }
}
