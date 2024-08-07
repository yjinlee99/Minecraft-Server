package com.minecraft.smallminecraft.member.controller;

import com.minecraft.smallminecraft.member.dtos.*;
import com.minecraft.smallminecraft.member.service.MemberService;
import jakarta.validation.Valid;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/account")
public class JoinController {
    private final MemberService memberService;

    public JoinController(MemberService memberService) {
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

}
