package com.minecraft.smallminecraft.member.entity;


import com.minecraft.smallminecraft.server.entity.Server;
import jakarta.persistence.*;
import jakarta.validation.constraints.Email;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.ArrayList;
import java.util.List;


@Entity
@Data
@AllArgsConstructor
@NoArgsConstructor
public class Member {

    @Id
    @Column(name = "member_id")
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id;

    @Column(nullable = false, unique = true)
    private String username;

    @Column(nullable = false)
    private String password;

    private String role;

    @Email
    @Column(nullable = false, unique = true)
    private String email;

    @OneToMany
    @JoinColumn(name = "member_id")
    private List<Server> servers = new ArrayList<>();
}
