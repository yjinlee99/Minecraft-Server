package com.minecraft.smallminecraft.member.controller;


import com.minecraft.smallminecraft.member.dtos.*;
import com.minecraft.smallminecraft.member.service.MemberService;
import jakarta.validation.Valid;
import jakarta.validation.constraints.NotBlank;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/account")
public class MemberController {

    private final MemberService memberService;

    public MemberController(MemberService memberService) {
        this.memberService = memberService;
    }

    /*
     * 회원 가입
     */
    @PostMapping("/v1/join")
    public ResponseEntity<Object> join(@Valid @RequestBody JoinDTO dto) {
        return memberService.join(dto);
    }

    /*
    * 아이디 중복 체크
    */
    @PostMapping("/v1/username_check")
    public ResponseEntity<Object> usernameCheck(@Valid @RequestBody UserCheckRequestDTO dto) {
        return memberService.uesrnameCheck(dto.getUsername());
    }

    /*
    * 이메일 중복 체크
    */
    @PostMapping("/v1/email_check")
    public ResponseEntity<Object> emailCheck(@Valid @RequestBody EmailCheckDTO dto) {
        return memberService.emilCheck(dto.getEmail());
    }

    /*
     * 비밀번호 찾기
     */
    @PostMapping("/v1/find_password")
    public ResponseEntity<Object> findPassword(@Valid @RequestBody PwRequestDTO dto) {
        return memberService.findPW(dto);
    }

    /*
    * 아이디 찾기
    */
    @PostMapping("/v1/find_username")
    public ResponseEntity<Object> findUsername(@Valid @RequestBody IDRequestDTO dto) {
        return memberService.findUsername(dto);
    }

    /*
     * 비밀번호 변경
     */
    @PostMapping("/v1/set_password")
    public ResponseEntity<Object> setPassword(@Valid @RequestBody SetPasswordDTO dto) {
        String username = SecurityContextHolder.getContext().getAuthentication().getName();
        return memberService.setPassword(dto, username);
    }
}
