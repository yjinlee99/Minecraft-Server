package com.minecraft.smallminecraft.order.entity;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.minecraft.smallminecraft.member.entity.Member;
import com.minecraft.smallminecraft.item.entity.Item;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Entity
@Data
@AllArgsConstructor
@NoArgsConstructor
public class Orders {

    @Id
    @Column(name = "order_id")
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id;

    private int price;

    private String receipt_id;

    private String status;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "item_id", updatable = false)
    @JsonIgnore // 순환 참조 방지
    private Item item;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "member_id", updatable = false)
    @JsonIgnore // 순환 참조 방지
    private Member member;
}
