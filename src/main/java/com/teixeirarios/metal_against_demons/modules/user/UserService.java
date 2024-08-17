package com.teixeirarios.metal_against_demons.modules.user;

import com.teixeirarios.metal_against_demons.modules.profile.ProfileEntity;
import com.teixeirarios.metal_against_demons.modules.profile.ProfileRepository;
import com.teixeirarios.metal_against_demons.modules.user.dtos.CreateUserDTO;
import com.teixeirarios.metal_against_demons.modules.user.dtos.ReadUserDTO;
import com.teixeirarios.metal_against_demons.modules.user.exceptions.EmailAlreadyExistsException;
import com.teixeirarios.metal_against_demons.modules.user.exceptions.UsernameAlreadyExistsException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
public class UserService implements UserDetailsService {

    @Autowired
    private UserRepository userRepository;

    @Autowired
    ProfileRepository profileRepository;

    @Autowired
    private PasswordEncoder passwordEncoder;

    @Transactional
    public ReadUserDTO createUser(CreateUserDTO createUserDTO) {

        Optional<UserEntity> existingUserByEmail = userRepository.findByEmail(createUserDTO.getEmail());
        if (existingUserByEmail.isPresent()) {
            throw new EmailAlreadyExistsException("Email " + createUserDTO.getUsername() + " is already in use.");
        }

        UserDetails existingUserByUsername = userRepository.findByUsername(createUserDTO.getUsername());
        if (existingUserByUsername != null) {
            throw new UsernameAlreadyExistsException("Username " + createUserDTO.getUsername() +  " is already in use.");
        }

        UserEntity user = new UserEntity();
        user.setUsername(createUserDTO.getUsername());
        user.setEmail(createUserDTO.getEmail());
        user.setPasswordHash(passwordEncoder.encode(createUserDTO.getPassword()));
        user.setRole(UserRole.USER);

        ProfileEntity profile = ProfileEntity.createNewProfile(user);
        ProfileEntity savedProfile = profileRepository.save(profile);
        user.setProfile(savedProfile);

        UserEntity savedUser = userRepository.save(user);
        return convertToDTO(savedUser);
    }

    public List<ReadUserDTO> getAllUsers() {
        List<UserEntity> users = userRepository.findAll();
        return users.stream()
                .map(this::convertToDTO)
                .collect(Collectors.toList());
    }

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        return userRepository.findByUsername(username);
    }

    private ReadUserDTO convertToDTO(UserEntity user) {
        ReadUserDTO userDTO = new ReadUserDTO();
        userDTO.setUserId(user.getUserId());
        userDTO.setUsername(user.getUsername());
        userDTO.setEmail(user.getEmail());
        userDTO.setProfile(user.getProfile());
        return userDTO;
    }


    //    public ReadUserDTO getUserByEmail(String email) {
    //        UserEntity user = userRepository
    //                .findByEmail(email)
    //                .orElseThrow(() -> new UserNotFoundException("User not found with email: " + email));
    //        return convertToDTO(user);
    //    }

    //    public ReadUserDTO getUserByUsername(String username) {
    //        UserDetails user = userRepository
    //                .findByUsername(username);
    //        return convertToDTO((UserEntity) user);
    //    }

    //    public ReadUserDTO getUserById(String userId) {
    //        UserEntity user = userRepository
    //                .findById(userId)
    //                .orElseThrow(() -> new UserNotFoundException("User not found with id: " + userId));
    //        return convertToDTO(user);
    //    }
}