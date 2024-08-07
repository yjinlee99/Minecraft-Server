package com.minecraft.smallminecraft.member.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.minecraft.smallminecraft.member.config.WithMockCustomUser;
import com.minecraft.smallminecraft.member.dtos.*;
import com.minecraft.smallminecraft.member.service.MemberService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.web.context.WebApplicationContext;

import static org.junit.jupiter.api.Assertions.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest
@ExtendWith(SpringExtension.class)
class SettingControllerTest {

    @Autowired
    private WebApplicationContext webApplicationContext;

    @Autowired
    private ObjectMapper objectMapper;

    @Autowired
    MemberService memberService;
    private MockMvc mockMvc;

    @BeforeEach
    public void setUp() {
        mockMvc = MockMvcBuilders.webAppContextSetup(webApplicationContext).build();

        memberService.join(new JoinDTO("exampleuser","example","example@example.com"));
    }
    @Test
    @WithMockCustomUser
    public void testSetPassword() throws Exception {
        //given
        SetPasswordDTO setPasswordDTO = new SetPasswordDTO("example", "newpassword");
        mockMvc.perform(post("/api/account/v1/set_password")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(setPasswordDTO)))
                .andExpect(status().isOk());
    }
}