package com.teixeirarios.metal_against_demons.modules.user.dtos;

import com.teixeirarios.metal_against_demons.modules.profile.ProfileEntity;
import lombok.Data;

@Data
public class ReadUserDTO {
    private String userId;
    private String username;
    private String email;
    private ProfileEntity profile;
}
