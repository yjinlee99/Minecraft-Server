package com.minecraft.smallminecraft.order.dtos;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class OrderApproveRequest {
    String receipt_id;
    int item_id;
}
