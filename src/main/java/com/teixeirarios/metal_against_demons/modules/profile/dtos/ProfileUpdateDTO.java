package com.teixeirarios.metal_against_demons.modules.profile.dtos;

import lombok.Data;
import java.util.Optional;

@Data
public class ProfileUpdateDTO {
    private Float money;
    private Integer level;
    private Integer experience;
    private Integer next_level_up;
    private Integer points;
    private Integer health;
    private Integer strength;
    private Integer dexterity;
    private Integer luck;
    private Integer current_stage;
}
