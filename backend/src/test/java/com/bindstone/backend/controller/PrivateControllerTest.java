package com.bindstone.backend.controller;

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
class PrivateControllerTest {

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
    void whenGetPrivateWithoutToken_thenReturnUnauthorized() throws Exception {
        mockMvc.perform(get("/private"))
                .andExpect(status().isUnauthorized());
    }

    @Test
    @WithMockUser
    void whenGetPrivateWithAuthenticatedUser_thenReturnHelloPrivate() throws Exception {
        mockMvc.perform(get("/private"))
                .andExpect(status().isOk())
                .andExpect(content().string("Hello PRIVATE"));
    }

    @Test
    @WithMockUser(roles = {"USER"})
    void whenGetPrivateWithUserRole_thenReturnHelloPrivate() throws Exception {
        mockMvc.perform(get("/private"))
                .andExpect(status().isOk())
                .andExpect(content().string("Hello PRIVATE"));
    }

    @Test
    @WithMockUser(roles = {"CONTINENTAL_ROLE_ADMIN"})
    void whenGetPrivateWithAdminRole_thenReturnHelloPrivate() throws Exception {
        mockMvc.perform(get("/private"))
                .andExpect(status().isOk())
                .andExpect(content().string("Hello PRIVATE"));
    }

    @Test
    @WithMockUser(roles = {"MANAGER"})
    void whenGetPrivateWithManagerRole_thenReturnHelloPrivate() throws Exception {
        mockMvc.perform(get("/private"))
                .andExpect(status().isOk())
                .andExpect(content().string("Hello PRIVATE"));
    }
}
