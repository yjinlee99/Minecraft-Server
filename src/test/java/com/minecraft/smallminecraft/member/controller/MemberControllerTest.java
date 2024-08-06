package com.minecraft.smallminecraft.member.controller;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.minecraft.smallminecraft.member.dtos.*;
import com.minecraft.smallminecraft.member.service.MemberService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.web.context.WebApplicationContext;

@SpringBootTest
@ExtendWith(SpringExtension.class)
public class MemberControllerTest {

    @Autowired
    private WebApplicationContext webApplicationContext;

    @Autowired
    private ObjectMapper objectMapper;

    @Autowired
    MemberService memberService;

    private MockMvc mockMvc;

    private JoinDTO joinDTO;
    private UserCheckRequestDTO userCheckRequestDTO;
    private EmailCheckDTO emailCheckDTO;
    private PwRequestDTO pwRequestDTO;
    private IDRequestDTO idRequestDTO;
    private SetPasswordDTO setPasswordDTO;

    @BeforeEach
    public void setUp() {
        mockMvc = MockMvcBuilders.webAppContextSetup(webApplicationContext).build();

        memberService.join(new JoinDTO("exampleuser","example","example@example.com"));

        joinDTO = new JoinDTO("testuser", "password", "testuser@example.com");
        userCheckRequestDTO = new UserCheckRequestDTO("test");
        emailCheckDTO = new EmailCheckDTO("example2@example.com");
        pwRequestDTO = new PwRequestDTO("exampleuser");
        idRequestDTO = new IDRequestDTO("example@example.com");
        setPasswordDTO = new SetPasswordDTO("oldpassword", "newpassword");
    }





    @Test
    public void testEmailCheck() throws Exception {
        mockMvc.perform(post("/api/account/v1/email_check")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(emailCheckDTO)))
                .andExpect(status().isOk());
    }


    @Test
    public void testSetPassword() throws Exception {
        mockMvc.perform(post("/api/account/v1/set_password")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(setPasswordDTO)))
                .andExpect(status().isOk());
    }
}