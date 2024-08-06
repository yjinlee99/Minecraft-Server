package com.minecraft.smallminecraft.member.controller;


import com.minecraft.smallminecraft.member.dtos.*;
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
public class SettingController {

    private final MemberService memberService;

    public SettingController(MemberService memberService) {
        this.memberService = memberService;
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
