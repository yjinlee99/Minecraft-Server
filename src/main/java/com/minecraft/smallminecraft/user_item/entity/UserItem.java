package com.minecraft.smallminecraft.user_item.entity;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.minecraft.smallminecraft.item.entity.Item;
import com.minecraft.smallminecraft.member.entity.Member;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Entity
@Data
@AllArgsConstructor
@NoArgsConstructor
public class UserItem {
    @Id
    @Column(name = "user_item_id")
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "member_id", updatable = false)
    @JsonIgnore // 순환 참조 방지
    private Member member;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "item_id", updatable = false)
    private Item item;
}
