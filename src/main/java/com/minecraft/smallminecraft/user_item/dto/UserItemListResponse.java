package com.minecraft.smallminecraft.user_item.dto;

import com.minecraft.smallminecraft.item.entity.Item;
import com.minecraft.smallminecraft.user_item.entity.UserItem;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class UserItemListResponse {
    public List<ItemDTO> items;
    public Integer selectedSkinId;
}
