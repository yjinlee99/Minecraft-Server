package com.minecraft.smallminecraft.server.dto;


import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class ServerLoadResponseDTO {


    private String difficulty;

    private int date;

    private int time;
}
