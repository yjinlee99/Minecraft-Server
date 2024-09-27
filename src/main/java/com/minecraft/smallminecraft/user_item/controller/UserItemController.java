package com.minecraft.smallminecraft.user_item.controller;

import com.minecraft.smallminecraft.user_item.dto.SelectSkinRequestDTO;
import com.minecraft.smallminecraft.user_item.service.UserItemService;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/user-item")
public class UserItemController {

    private final UserItemService userItemService;

    public UserItemController(UserItemService userItemService) {
        this.userItemService = userItemService;
    }

    @GetMapping
    public ResponseEntity<Object> getMySkins () {
        String username = SecurityContextHolder.getContext().getAuthentication().getName();
        return userItemService.getMySkins(username);
    }

    @PostMapping("/select")
    public ResponseEntity<Object> selectSkin(@RequestBody SelectSkinRequestDTO selectSkinRequestDTO) {
        String username = SecurityContextHolder.getContext().getAuthentication().getName();
        return userItemService.selectSkin(selectSkinRequestDTO, username);
    }
}
