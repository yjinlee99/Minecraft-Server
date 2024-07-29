package com.minecraft.smallminecraft.server.entity;


import com.minecraft.smallminecraft.member.entity.Member;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Entity
@Data
@NoArgsConstructor
@AllArgsConstructor
public class Server {

    @Id @Column(name = "server_id")
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id;

    @Column(nullable = false)
    private String serverName;

    @Column(nullable = false)
    private String password;

    @Column(nullable = false)
    private String difficulty;

    @Column(nullable = false)
    private int date;

    @Column(nullable = false)
    private int time;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "member_id")
    private Member member;


}
