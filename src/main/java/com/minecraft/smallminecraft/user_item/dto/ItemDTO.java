package com.minecraft.smallminecraft.user_item.dto;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class ItemDTO {
    private int id;
    private String name;
    private String img;

    public ItemDTO(int id, String name, String img) {
        this.id = id;
        this.name = name;
        this.img = img;
    }
}
