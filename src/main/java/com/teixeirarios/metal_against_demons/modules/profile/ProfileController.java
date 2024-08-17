package com.teixeirarios.metal_against_demons.modules.profile;

import com.teixeirarios.metal_against_demons.modules.profile.dtos.ProfileUpdateDTO;
import com.teixeirarios.metal_against_demons.modules.user.UserEntity;
import com.teixeirarios.metal_against_demons.modules.user.dtos.ReadUserDTO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/profile")
public class ProfileController {

    @Autowired
    ProfileRepository profileRepository;

    @GetMapping("/list")
    public ResponseEntity<List<ProfileEntity>> listProfiles() {
        List<ProfileEntity> profiles = profileRepository.findAll();
        return ResponseEntity.ok(profiles);
    }

    @PatchMapping()
    public ResponseEntity<ProfileEntity> updateProfile(@RequestBody ProfileUpdateDTO profileUpdateDTO) {

        var authentication = SecurityContextHolder.getContext().getAuthentication();
        if (authentication == null) return ResponseEntity.notFound().build();

        UserEntity userEntity = (UserEntity) authentication.getPrincipal();
        if (userEntity == null) return ResponseEntity.notFound().build();

        ProfileEntity profile = userEntity.getProfile();

        if (profileUpdateDTO.getMoney() != null) {
            profile.setMoney(profileUpdateDTO.getMoney());
        }

        if (profileUpdateDTO.getLevel() != null) {
            profile.setLevel(profileUpdateDTO.getLevel());
        }

        if (profileUpdateDTO.getExperience() != null) {
            profile.setExperience(profileUpdateDTO.getExperience());
        }

        if (profileUpdateDTO.getNext_level_up() != null) {
            profile.setNext_level_up(profileUpdateDTO.getNext_level_up());
        }

        if (profileUpdateDTO.getPoints() != null) {
            profile.setPoints(profileUpdateDTO.getPoints());
        }

        if (profileUpdateDTO.getHealth() != null) {
            profile.setHealth(profileUpdateDTO.getHealth());
        }

        if (profileUpdateDTO.getStrength() != null) {
            profile.setStrength(profileUpdateDTO.getStrength());
        }

        if (profileUpdateDTO.getDexterity() != null) {
            profile.setDexterity(profileUpdateDTO.getDexterity());
        }

        if (profileUpdateDTO.getLuck() != null) {
            profile.setLuck(profileUpdateDTO.getLuck());
        }

        if (profileUpdateDTO.getCurrent_stage() != null) {
            profile.setCurrent_stage(profileUpdateDTO.getCurrent_stage());
        }

        ProfileEntity updatedProfile = profileRepository.save(profile);
        return ResponseEntity.ok(updatedProfile);
    }
}
