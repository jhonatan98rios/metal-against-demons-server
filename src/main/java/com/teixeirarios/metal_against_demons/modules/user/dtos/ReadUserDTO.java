package com.teixeirarios.metal_against_demons.modules.user.dtos;

import lombok.Data;

@Data
public class ReadUserDTO {
    private Long userId;
    private String username;
    private String email;
}
