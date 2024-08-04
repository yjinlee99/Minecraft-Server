package com.minecraft.smallminecraft.member.dtos;

import jakarta.validation.constraints.Email;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class EmailCheckDTO {
    @Email
    String email;
}
