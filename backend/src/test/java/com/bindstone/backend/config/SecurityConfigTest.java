package com.bindstone.backend.config;

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
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@SpringBootTest
@AutoConfigureWebMvc
class SecurityConfigTest {

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
    void whenAccessPublicEndpoint_thenAllowAll() throws Exception {
        mockMvc.perform(get("/public"))
                .andExpect(status().isOk());
    }

    @Test
    void whenAccessPrivateEndpointWithoutAuth_thenUnauthorized() throws Exception {
        mockMvc.perform(get("/private"))
                .andExpect(status().isUnauthorized());
    }

    @Test
    void whenAccessAdminEndpointWithoutAuth_thenUnauthorized() throws Exception {
        mockMvc.perform(get("/admin"))
                .andExpect(status().isUnauthorized());
    }

    @Test
    @WithMockUser
    void whenAccessPrivateEndpointWithValidAuth_thenAllowed() throws Exception {
        mockMvc.perform(get("/private"))
                .andExpect(status().isOk());
    }

    @Test
    @WithMockUser(roles = {"USER"})
    void whenAccessAdminEndpointWithUserRole_thenForbidden() throws Exception {
        mockMvc.perform(get("/admin"))
                .andExpect(status().isForbidden());
    }

    @Test
    @WithMockUser(roles = {"CONTINENTAL_ROLE_ADMIN"})
    void whenAccessAdminEndpointWithAdminRole_thenAllowed() throws Exception {
        mockMvc.perform(get("/admin"))
                .andExpect(status().isOk());
    }

    @Test
    void whenAccessUnknownEndpoint_thenUnauthorized() throws Exception {
        mockMvc.perform(get("/unknown"))
                .andExpect(status().isUnauthorized());
    }

    @Test
    @WithMockUser(roles = {"CONTINENTAL_ROLE_ADMIN"})
    void whenOptionsRequest_thenCorsHeadersPresent() throws Exception {
        mockMvc.perform(options("/public")
                .header("Origin", "http://localhost:3000")
                .header("Access-Control-Request-Method", "GET"))
                .andExpect(status().isOk());
    }

    @Test
    void whenPostRequestWithoutAuth_thenForbidden() throws Exception {
        mockMvc.perform(post("/private"))
                .andExpect(status().isForbidden());
    }

    @Test
    void whenPutRequestWithoutAuth_thenForbidden() throws Exception {
        mockMvc.perform(put("/private"))
                .andExpect(status().isForbidden());
    }

    @Test
    void whenDeleteRequestWithoutAuth_thenForbidden() throws Exception {
        mockMvc.perform(delete("/private"))
                .andExpect(status().isForbidden());
    }
}
