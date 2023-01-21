package com.example.web;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockHttpServletRequestBuilder;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.request.RequestPostProcessor;

import static org.junit.jupiter.api.Assertions.*;
import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.httpBasic;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest
@AutoConfigureMockMvc
class WebControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @Test
    void expectOK_WhenAccessAsyncAPI() throws Exception {

        MockHttpServletRequestBuilder httpServletRequestBuilder = MockMvcRequestBuilders.get("/async");
        RequestPostProcessor requestAuthPostProcessor = httpBasic("user", "user");
        MockHttpServletRequestBuilder requestBuilder = httpServletRequestBuilder.with(requestAuthPostProcessor);

        mockMvc
                .perform(requestBuilder)
                .andExpect(status().isOk());
    }

    @Test
    void executeWithExternalThread_Runnable() throws Exception {

        MockHttpServletRequestBuilder httpServletRequestBuilder =
                MockMvcRequestBuilders.get("/asyncRunnable");
        RequestPostProcessor requestAuthPostProcessor = httpBasic("user", "user");
        MockHttpServletRequestBuilder requestBuilder = httpServletRequestBuilder.with(requestAuthPostProcessor);

        mockMvc
                .perform(requestBuilder)
                .andExpect(status().isOk());
    }
}