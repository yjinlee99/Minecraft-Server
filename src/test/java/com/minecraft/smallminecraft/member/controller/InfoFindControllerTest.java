package com.minecraft.smallminecraft.member.controller;

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

import static org.junit.jupiter.api.Assertions.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest
@ExtendWith(SpringExtension.class)
class InfoFindControllerTest {

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
    public void testFindPassword() throws Exception {
        //given
        PwRequestDTO pwRequestDTO = new PwRequestDTO("exampleuser");

        mockMvc.perform(post("/api/account/v1/find_password")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(pwRequestDTO)))
                .andExpect(status().isOk());
    }

    @Test
    public void testFindUsername() throws Exception {
        //given
        IDRequestDTO idRequestDTO = new IDRequestDTO("example@example.com");

        mockMvc.perform(post("/api/account/v1/find_username")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(idRequestDTO)))
                .andExpect(status().isOk());
    }


}