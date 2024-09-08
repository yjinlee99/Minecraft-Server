package com.minecraft.smallminecraft.member.controller;

import org.springframework.http.HttpStatus;
import org.springframework.http.HttpStatusCode;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/verity")
public class VerifyController {

    @GetMapping("/v1/token")
    public ResponseEntity<Object> verify_token() {
        return ResponseEntity.status(HttpStatus.OK)
                .build();
    }
}
