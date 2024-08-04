package com.minecraft.smallminecraft.member.service;


import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;
import static org.mockito.Mockito.verify;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;

import com.minecraft.smallminecraft.member.dtos.*;
import com.minecraft.smallminecraft.member.entity.Member;
import com.minecraft.smallminecraft.member.repository.MemberRepository;
import com.minecraft.smallminecraft.response.ErrorResponse;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;

@ExtendWith(MockitoExtension.class)
public class MemberServiceTest {

    @Mock
    private MemberRepository memberRepository;

    @Mock
    private BCryptPasswordEncoder bCryptPasswordEncoder;

    @Mock
    private SendService sendService;

    @InjectMocks
    private MemberService memberService;

    private JoinDTO joinDTO;
    private PwRequestDTO pwRequestDTO;
    private SetPasswordDTO setPasswordDTO;
    private IDRequestDTO idRequestDTO;

    @BeforeEach
    public void setUp() {
        joinDTO = new JoinDTO();
        joinDTO.setUsername("testuser");
        joinDTO.setPassword("password");
        joinDTO.setEmail("testuser@example.com");

        pwRequestDTO = new PwRequestDTO();
        pwRequestDTO.setUsername("testuser");

        setPasswordDTO = new SetPasswordDTO();
        setPasswordDTO.setPassword("oldpassword");
        setPasswordDTO.setNew_password("newpassword");

        idRequestDTO = new IDRequestDTO();
        idRequestDTO.setEmail("testuser@example.com");
    }

    @Test
    @DisplayName("회원가입 실패 (아이디 중복)")
    public void testJoin_UsernameExists() {
        // given
        when(memberRepository.findByUsername(joinDTO.getUsername())).thenReturn(new Member());

        // when
        ResponseEntity<Object> response = memberService.join(joinDTO);

        // then
        assertEquals(HttpStatus.BAD_REQUEST, response.getStatusCode());
        ErrorResponse errorResponse = (ErrorResponse) response.getBody();
        assertNotNull(errorResponse);
        assertEquals("이미 존재하는 아이디입니다.", errorResponse.getMessage());
    }

    @Test
    @DisplayName("회원가입 실패 (이메일 중복)")
    public void testJoin_EmailExists() {
        // given
        when(memberRepository.findByUsername(joinDTO.getUsername())).thenReturn(null);
        when(memberRepository.findByEmail(joinDTO.getEmail())).thenReturn(new Member());

        // when
        ResponseEntity<Object> response = memberService.join(joinDTO);

        // then
        assertEquals(HttpStatus.BAD_REQUEST, response.getStatusCode());
        ErrorResponse errorResponse = (ErrorResponse) response.getBody();
        assertNotNull(errorResponse);
        assertEquals("이미 존재하는 이메일입니다.", errorResponse.getMessage());
    }

    @Test
    @DisplayName("회원가입 성공")
    public void testJoin_Success() {
        // given
        when(memberRepository.findByUsername(joinDTO.getUsername())).thenReturn(null);
        when(memberRepository.findByEmail(joinDTO.getEmail())).thenReturn(null);
        when(bCryptPasswordEncoder.encode(joinDTO.getPassword())).thenReturn("encodedPassword");

        Member savedMember = new Member();
        savedMember.setUsername(joinDTO.getUsername());
        savedMember.setPassword("encodedPassword");
        savedMember.setEmail(joinDTO.getEmail());
        savedMember.setRole("user");

        when(memberRepository.save(any(Member.class))).thenReturn(savedMember);

        // when
        ResponseEntity<Object> response = memberService.join(joinDTO);

        // then
        assertEquals(HttpStatus.OK, response.getStatusCode());
        verify(memberRepository).save(any(Member.class));
    }

    @Test
    @DisplayName("비밀번호 찾기 실패 (존재하지 않는 아이디)")
    public void testFindPW_UserNotFound() {
        // given
        when(memberRepository.findByUsername(pwRequestDTO.getUsername())).thenReturn(null);

        // when
        ResponseEntity<Object> response = memberService.findPW(pwRequestDTO);

        // then
        assertEquals(HttpStatus.BAD_REQUEST, response.getStatusCode());
        ErrorResponse errorResponse = (ErrorResponse) response.getBody();
        assertNotNull(errorResponse);
        assertEquals("존재하지 않는 아이디입니다.", errorResponse.getMessage());
    }

    @Test
    @DisplayName("비밀번호 찾기 성공")
    public void testFindPW_Success() {
        // given
        Member member = new Member();
        member.setUsername("testuser");
        member.setEmail("testuser@example.com");
        when(memberRepository.findByUsername(pwRequestDTO.getUsername())).thenReturn(member);

        MailDTO mailDTO = new MailDTO();
        when(sendService.createMailAndChargePassword(any(PwRequestDTO.class), any(String.class))).thenReturn(mailDTO);

        // when
        ResponseEntity<Object> response = memberService.findPW(pwRequestDTO);

        // then
        assertEquals(HttpStatus.OK, response.getStatusCode());
        verify(sendService).mailSend(mailDTO);
    }

    @Test
    @DisplayName("비밀번호 변경 실패 (회원정보 못찾음)")
    public void testSetPassword_UserNotFound() {
        // given
        when(memberRepository.findByUsername("testuser")).thenReturn(null);

        // when
        ResponseEntity<Object> response = memberService.setPassword(setPasswordDTO, "testuser");

        // then
        assertEquals(HttpStatus.BAD_REQUEST, response.getStatusCode());
        ErrorResponse errorResponse = (ErrorResponse) response.getBody();
        assertNotNull(errorResponse);
        assertEquals("존재하지 않는 회원입니다.", errorResponse.getMessage());
    }

    @Test
    @DisplayName("비밀번호 변경 실패 (비밀번호 틀림)")
    public void testSetPassword_WrongPassword() {
        // given
        Member member = new Member();
        member.setUsername("testuser");
        member.setPassword(bCryptPasswordEncoder.encode("correctpassword"));
        when(memberRepository.findByUsername("testuser")).thenReturn(member);
        when(bCryptPasswordEncoder.matches("oldpassword", member.getPassword())).thenReturn(false);

        // when
        ResponseEntity<Object> response = memberService.setPassword(setPasswordDTO, "testuser");

        // then
        assertEquals(HttpStatus.BAD_REQUEST, response.getStatusCode());
        ErrorResponse errorResponse = (ErrorResponse) response.getBody();
        assertNotNull(errorResponse);
        assertEquals("비밀번호가 틀렸습니다.", errorResponse.getMessage());
    }

    @Test
    @DisplayName("비밀번호 변경 성공")
    public void testSetPassword_Success() {
        // given
        Member member = new Member();
        member.setUsername("testuser");
        member.setPassword(bCryptPasswordEncoder.encode("oldpassword"));
        when(memberRepository.findByUsername("testuser")).thenReturn(member);
        when(bCryptPasswordEncoder.matches("oldpassword", member.getPassword())).thenReturn(true);
        when(bCryptPasswordEncoder.encode("newpassword")).thenReturn("newencodedpassword");

        // when
        ResponseEntity<Object> response = memberService.setPassword(setPasswordDTO, "testuser");

        // then
        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertEquals("newencodedpassword", member.getPassword());
    }

    @Test
    @DisplayName("아이디 찾기 (존재하지 않는 회원)")
    public void testFindUsername_UserNotFound() {
        // given
        when(memberRepository.findByEmail(idRequestDTO.getEmail())).thenReturn(null);

        // when
        ResponseEntity<Object> response = memberService.findUsername(idRequestDTO);

        // then
        assertEquals(HttpStatus.BAD_REQUEST, response.getStatusCode());
        ErrorResponse errorResponse = (ErrorResponse) response.getBody();
        assertNotNull(errorResponse);
        assertEquals("존재하지 않는 회원입니다.", errorResponse.getMessage());
    }

    @Test
    @DisplayName("아이디 찾기 성공")
    public void testFindUsername_Success() {
        // given
        Member member = new Member();
        member.setUsername("testuser");
        member.setEmail("testuser@example.com");
        when(memberRepository.findByEmail(idRequestDTO.getEmail())).thenReturn(member);

        // when
        ResponseEntity<Object> response = memberService.findUsername(idRequestDTO);

        // then
        assertEquals(HttpStatus.OK, response.getStatusCode());
        IDResponseDTO responseDTO = (IDResponseDTO) response.getBody();
        assertNotNull(responseDTO);
        assertEquals("testuser", responseDTO.getUsername());
    }

    @Test
    @DisplayName("아이디 중복 체크 실패 (이미 존재하는 아이디)")
    public void testUsernameCheck_UserExists() {
        // given
        when(memberRepository.findByUsername("testuser")).thenReturn(new Member());

        // when
        ResponseEntity<Object> response = memberService.uesrnameCheck("testuser");

        // then
        assertEquals(HttpStatus.BAD_REQUEST, response.getStatusCode());
        ErrorResponse errorResponse = (ErrorResponse) response.getBody();
        assertNotNull(errorResponse);
        assertEquals("이미 존재하는 아이디입니다.", errorResponse.getMessage());
    }

    @Test
    @DisplayName("아이디 중복 체크 성공")
    public void testUsernameCheck_UserNotExists() {
        // given
        when(memberRepository.findByUsername("testuser")).thenReturn(null);

        // when
        ResponseEntity<Object> response = memberService.uesrnameCheck("testuser");

        // then
        assertEquals(HttpStatus.OK, response.getStatusCode());
    }

    @Test
    @DisplayName("이메일 중복 체크 실패 (이미 존재하는 이메일)")
    public void testEmailCheck_EmailExists() {
        // given
        when(memberRepository.findByEmail("testuser@example.com")).thenReturn(new Member());

        // when
        ResponseEntity<Object> response = memberService.emilCheck("testuser@example.com");

        // then
        assertEquals(HttpStatus.BAD_REQUEST, response.getStatusCode());
        ErrorResponse errorResponse = (ErrorResponse) response.getBody();
        assertNotNull(errorResponse);
        assertEquals("이미 존재하는 이메일입니다.", errorResponse.getMessage());
    }

    @Test
    @DisplayName("이메일 중복 체크 성공")
    public void testEmailCheck_EmailNotExists() {
        // given
        when(memberRepository.findByEmail("testuser@example.com")).thenReturn(null);

        // when
        ResponseEntity<Object> response = memberService.emilCheck("testuser@example.com");

        // then
        assertEquals(HttpStatus.OK, response.getStatusCode());
    }
}
