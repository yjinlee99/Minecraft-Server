package com.minecraft.smallminecraft.server.entity;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Entity
@Data
@AllArgsConstructor
@NoArgsConstructor
public class Server {
    @Id
    @Column(name = "server_id")
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id;

    private String name;
    private String data;
}
