package com.minecraft.smallminecraft.member.entity;


import com.fasterxml.jackson.annotation.JsonIgnore;
import com.minecraft.smallminecraft.item.entity.Item;
import com.minecraft.smallminecraft.server.entity.Server;
import com.minecraft.smallminecraft.user_item.entity.UserItem;
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

    @OneToMany(fetch = FetchType.LAZY)
    @JoinColumn(name = "member_id")
    @JsonIgnore // 순환 참조 방지
    private List<Server> servers = new ArrayList<>();

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "item_id", updatable = false)
    private Item item;

    @OneToMany(fetch = FetchType.LAZY, mappedBy = "member")
    @JsonIgnore // 순환 참조 방지
    private List<UserItem> userItems = new ArrayList<>();
}
