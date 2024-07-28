package com.teixeirarios.metal_against_demons.modules.user;

import lombok.Data;

@Data
public class CreateUserDTO {
    private String username;
    private String email;
    private String password;
}
