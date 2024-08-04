package com.minecraft.smallminecraft.member.service;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

import com.minecraft.smallminecraft.member.dtos.MailDTO;
import com.minecraft.smallminecraft.member.dtos.PwRequestDTO;
import com.minecraft.smallminecraft.member.entity.Member;
import com.minecraft.smallminecraft.member.repository.MemberRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;

@ExtendWith(MockitoExtension.class)
public class SendServiceTest {

    @Mock
    private JavaMailSender mailSender;

    @Mock
    private BCryptPasswordEncoder passwordEncoder;

    @Mock
    private MemberRepository memberRepository;

    @InjectMocks
    private SendService sendService;

    private PwRequestDTO pwRequestDTO;
    private Member member;

    @BeforeEach
    public void setUp() {
        pwRequestDTO = new PwRequestDTO();
        pwRequestDTO.setUsername("testuser");

        member = new Member();
        member.setUsername("testuser");
        member.setEmail("testuser@example.com");
        member.setPassword("oldpassword");
        member.setRole("user");
    }

    @Test
    public void testCreateMailAndChargePassword() {
        // given
        when(memberRepository.findByUsername(pwRequestDTO.getUsername())).thenReturn(member);
        when(passwordEncoder.encode(any(String.class))).thenReturn("encodedPassword");

        // when
        MailDTO mailDTO = sendService.createMailAndChargePassword(pwRequestDTO, member.getEmail());

        // then
        assertNotNull(mailDTO);
        assertEquals(member.getEmail(), mailDTO.getAddress());
        verify(memberRepository).findByUsername(pwRequestDTO.getUsername());
        verify(passwordEncoder).encode(any(String.class));
        assertEquals("encodedPassword", member.getPassword());
    }

    @Test
    public void testUpdatePassword() {
        // given
        when(memberRepository.findByUsername(pwRequestDTO.getUsername())).thenReturn(member);
        when(passwordEncoder.encode(any(String.class))).thenReturn("encodedPassword");

        // when
        sendService.updatePassword("newpassword", pwRequestDTO);

        // then
        verify(memberRepository).findByUsername(pwRequestDTO.getUsername());
        verify(passwordEncoder).encode("newpassword");
        assertEquals("encodedPassword", member.getPassword());
    }

    @Test
    public void testMailSend() {
        // given
        MailDTO mailDTO = new MailDTO();
        mailDTO.setAddress("testuser@example.com");
        mailDTO.setTitle("Test Title");
        mailDTO.setMessage("Test Message");

        // when
        sendService.mailSend(mailDTO);

        // then
        verify(mailSender, times(1)).send(any(SimpleMailMessage.class));
    }
}