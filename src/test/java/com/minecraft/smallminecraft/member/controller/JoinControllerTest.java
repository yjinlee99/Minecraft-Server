package com.minecraft.smallminecraft.member.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.minecraft.smallminecraft.member.dtos.*;
import com.minecraft.smallminecraft.member.service.MemberService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.web.context.WebApplicationContext;

import static org.junit.jupiter.api.Assertions.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest
@ExtendWith(SpringExtension.class)
class JoinControllerTest {

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
    @DisplayName("회원가입")
    public void testJoin() throws Exception {
        //given
        JoinDTO joinDTO = new JoinDTO("testuser", "password", "testuser@example.com");

        mockMvc.perform(post("/api/account/v1/join")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(joinDTO)))
                .andExpect(status().isOk());
    }

    @Test
    @DisplayName("아이디 중복 체크")
    public void testUsernameCheck() throws Exception {
        //given
        UserCheckRequestDTO userCheckRequestDTO = new UserCheckRequestDTO("test");

        mockMvc.perform(post("/api/account/v1/username_check")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(userCheckRequestDTO)))
                .andExpect(status().isOk());
    }

    @Test
    public void testEmailCheck() throws Exception {
        //given
        EmailCheckDTO emailCheckDTO = new EmailCheckDTO("example2@example.com");

        mockMvc.perform(post("/api/account/v1/email_check")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(emailCheckDTO)))
                .andExpect(status().isOk());
    }

}