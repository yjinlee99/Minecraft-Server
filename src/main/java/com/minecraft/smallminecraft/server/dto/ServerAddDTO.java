package com.minecraft.smallminecraft.server.dto;


import jakarta.validation.constraints.NotBlank;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class ServerAddDTO {

    @NotBlank
    private String server_name;

    @NotBlank
    private String password;

    @NotBlank
    private String difficulty;

}
