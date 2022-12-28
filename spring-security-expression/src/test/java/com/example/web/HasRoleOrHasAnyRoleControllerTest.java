package com.example.web;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.web.context.WebApplicationContext;

import static org.springframework.security.test.web.servlet.setup.SecurityMockMvcConfigurers.springSecurity;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@ExtendWith(SpringExtension.class)
@SpringBootTest(
        webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT
)
@AutoConfigureMockMvc
class HasRoleOrHasAnyRoleControllerTest {

    @Autowired
    private WebApplicationContext context;

    @Autowired
    private MockMvc mvc;

    @BeforeEach
    public void setup() {
        mvc = MockMvcBuilders
                .webAppContextSetup(context)
                .apply(springSecurity())
                .build();
    }

    /**
     * username - здесь можно указать любое значение учетной записи, так как эта учетная запись - заглушка,
     * roles - здесь указываются роли, которые определены в фильтрах, так как именно это значение
     * будет сравниваться со значениями указанными в фильтрах безопасности для конктретных точек
     */
    @WithMockUser(username = "admin", roles = {"ADMIN"})
    @Test
    void getProtectedHasRole() throws Exception {

        mvc
                .perform(MockMvcRequestBuilders.get("/has/role"))
                .andExpect(status().isOk());

    }

    @WithMockUser(username = "admin", roles = {"wrong"})
    @Test
    void getProtectedHasRoleFailed() throws Exception {

        mvc
                .perform(MockMvcRequestBuilders.get("/has/role"))
                .andExpect(status().isForbidden());

    }
}