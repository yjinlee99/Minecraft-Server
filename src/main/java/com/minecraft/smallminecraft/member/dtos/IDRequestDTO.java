package com.minecraft.smallminecraft.member.dtos;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import lombok.Data;

@Data
public class IDRequestDTO {
    @NotBlank @Email
    String email;
}
