package com.minecraft.smallminecraft.member.controller;

import com.minecraft.smallminecraft.member.dtos.IDRequestDTO;
import com.minecraft.smallminecraft.member.dtos.PwRequestDTO;
import com.minecraft.smallminecraft.member.service.MemberService;
import jakarta.validation.Valid;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/account")
public class InfoFindController {
    private final MemberService memberService;

    public InfoFindController(MemberService memberService) {
        this.memberService = memberService;
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
}
