package com.teixeirarios.metal_against_demons.modules.profile;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.teixeirarios.metal_against_demons.modules.user.UserEntity;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;

@Entity
@Table(name = "profiles")
@Data
@NoArgsConstructor
@AllArgsConstructor
@EqualsAndHashCode(of = "profileId")
public class ProfileEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    @Column(unique = true, nullable = false)
    private String profileId;

    @Column(nullable = false)
    private float money;

    @Column(nullable = false)
    private int level;

    @Column(nullable = false)
    private int experience;

    @Column(nullable = false)
    private int next_level_up;

    @Column(nullable = false)
    private int points;

    @Column(nullable = false)
    private int health;

    @Column(nullable = false)
    private int strength;

    @Column(nullable = false)
    private int dexterity;

    @Column(nullable = false)
    private int luck;

    @Column(nullable = false)
    private int current_stage;

    @OneToOne(mappedBy = "profile", cascade = CascadeType.ALL, orphanRemoval = true)
    @JsonIgnore
    private UserEntity user;

    public static ProfileEntity createNewProfile(UserEntity userEntity) {
        ProfileEntity profileEntity = new ProfileEntity();
        profileEntity.money = 0;
        profileEntity.level = 1;
        profileEntity.experience = 0;
        profileEntity.next_level_up = 10;
        profileEntity.points = 0;
        profileEntity.health = 1000;
        profileEntity.strength = 1;
        profileEntity.dexterity = 1;
        profileEntity.luck = 1;
        profileEntity.current_stage = 1;
        profileEntity.user = userEntity;

        return profileEntity;
    }
}
