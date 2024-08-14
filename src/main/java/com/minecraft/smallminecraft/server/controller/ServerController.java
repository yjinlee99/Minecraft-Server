package com.minecraft.smallminecraft.server.controller;

import com.minecraft.smallminecraft.server.dtos.AddServerDTO;
import com.minecraft.smallminecraft.server.service.ServerService;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/server")
public class ServerController {

    private final ServerService serverService;

    public ServerController(ServerService serverService) {
        this.serverService = serverService;
    }

    @PostMapping("v1/add")
    public ResponseEntity<Object> AddServer(@RequestBody AddServerDTO addServerDTO) {
        String username = SecurityContextHolder.getContext().getAuthentication().getName();
        return serverService.addServer(addServerDTO, username);
    }

    @PostMapping("v1/update/map")
    public ResponseEntity<Object> UpdateServer(@RequestPart(value= "map"))
}
