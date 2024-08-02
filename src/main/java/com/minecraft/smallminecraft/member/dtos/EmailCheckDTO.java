package com.minecraft.smallminecraft.member.dtos;

import jakarta.validation.constraints.Email;
import lombok.Data;

@Data
public class EmailCheckDTO {
    @Email
    String email;
}
