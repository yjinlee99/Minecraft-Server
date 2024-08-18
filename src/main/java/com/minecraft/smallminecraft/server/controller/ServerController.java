package com.minecraft.smallminecraft.server.controller;

import com.minecraft.smallminecraft.server.dtos.AddServerDTO;
import com.minecraft.smallminecraft.server.service.ServerService;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

@RestController
@RequestMapping("/api/server")
public class ServerController {

    private final ServerService serverService;

    public ServerController(ServerService serverService) {
        this.serverService = serverService;
    }

    @PostMapping("/v2/add")
    public ResponseEntity<Object> AddServer(@RequestBody AddServerDTO addServerDTO) {
        String username = SecurityContextHolder.getContext().getAuthentication().getName();
        return serverService.addServer(addServerDTO, username);
    }

    @PostMapping("/v1/update/map/{name}")
    public ResponseEntity<Object> UpdateMap(@PathVariable("name") String servername, @RequestPart(value= "map")MultipartFile file) {
        String username = SecurityContextHolder.getContext().getAuthentication().getName();
        return serverService.updateMap(username, servername,file);
    }

    @PostMapping("/v1/update/info/{name}")
    public ResponseEntity<Object> UpdateInfo(@PathVariable("name") String servername, @RequestPart(value= "info")MultipartFile file) {
        String username = SecurityContextHolder.getContext().getAuthentication().getName();
        return serverService.updateInfo(username, servername,file);
    }
}
