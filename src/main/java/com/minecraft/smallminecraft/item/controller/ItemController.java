package com.minecraft.smallminecraft.item.controller;

import com.minecraft.smallminecraft.item.service.ItemService;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/store")
public class ItemController {
    private final ItemService itemService;

    public ItemController(ItemService itemService) {
        this.itemService = itemService;
    }

    @GetMapping
    public ResponseEntity<Object> showList() {
        String username = SecurityContextHolder.getContext().getAuthentication().getName();
        return itemService.showList(username);
    }
}
