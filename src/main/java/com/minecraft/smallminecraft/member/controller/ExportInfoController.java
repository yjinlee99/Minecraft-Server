package com.minecraft.smallminecraft.member.controller;

import com.minecraft.smallminecraft.member.service.MemberService;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/account")
public class ExportInfoController {
    private final MemberService memberService;

    public ExportInfoController(MemberService memberService) {
        this.memberService = memberService;
    }

    @GetMapping("/v1/skin")
    public ResponseEntity<Object> exportSkin() {
        String username = SecurityContextHolder.getContext().getAuthentication().getName();
        return memberService.exportSkin(username);
    }
}
