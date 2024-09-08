package com.minecraft.smallminecraft.order.dtos;

import com.minecraft.smallminecraft.member.entity.Member;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class OrderResponse {
    int orderId;
    Member member;
}
