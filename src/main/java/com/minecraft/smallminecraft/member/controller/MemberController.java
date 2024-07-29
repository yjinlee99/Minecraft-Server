package com.minecraft.smallminecraft.member.controller;


import com.minecraft.smallminecraft.member.dtos.JoinDTO;
import com.minecraft.smallminecraft.member.dtos.PwRequestDTO;
import com.minecraft.smallminecraft.member.dtos.SetPasswordDTO;
import com.minecraft.smallminecraft.member.service.MemberService;
import jakarta.validation.Valid;
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
    @PostMapping("/v2/join")
    public ResponseEntity<Object> join(@Valid @RequestBody JoinDTO dto) {
        return memberService.join(dto);
    }

    /*
     * 비밀번호 찾기
     */
    @PostMapping("/v1/password")
    public ResponseEntity<Object> findPassword(@Valid @RequestBody PwRequestDTO dto) {
        return memberService.findPW(dto);
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
