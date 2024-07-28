package com.teixeirarios.metal_against_demons.modules.user;

import com.teixeirarios.metal_against_demons.modules.user.dtos.CreateUserDTO;
import com.teixeirarios.metal_against_demons.modules.user.dtos.ReadUserDTO;
import com.teixeirarios.metal_against_demons.modules.user.exceptions.EmailAlreadyExistsException;
import com.teixeirarios.metal_against_demons.modules.user.exceptions.UserNotFoundException;
import com.teixeirarios.metal_against_demons.modules.user.exceptions.UsernameAlreadyExistsException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
public class UserService {
    @Autowired
    private UserRepository userRepository;

    @Autowired
    private PasswordEncoder passwordEncoder;

    @Transactional
    public ReadUserDTO createUser(CreateUserDTO createUserDTO) {
        Optional<UserEntity> existingUserByEmail = userRepository.findByEmail(createUserDTO.getEmail());
        if (existingUserByEmail.isPresent()) {
            throw new EmailAlreadyExistsException("Email is already in use.");
        }

        Optional<UserEntity> existingUserByUsername = userRepository.findByUsername(createUserDTO.getUsername());
        if (existingUserByUsername.isPresent()) {
            throw new UsernameAlreadyExistsException("Username is already in use.");
        }

        UserEntity user = new UserEntity();
        user.setUsername(createUserDTO.getUsername());
        user.setEmail(createUserDTO.getEmail());
        user.setPasswordHash(passwordEncoder.encode(createUserDTO.getPassword()));
        UserEntity savedUser = userRepository.save(user);

        return convertToDTO(savedUser);
    }

    public ReadUserDTO getUserByEmail(String email) {
        UserEntity user = userRepository
                .findByEmail(email)
                .orElseThrow(() -> new UserNotFoundException("User not found with email: " + email));
        return convertToDTO(user);
    }

    public ReadUserDTO getUserByUsername(String username) {
        UserEntity user = userRepository
                .findByUsername(username)
                .orElseThrow(() -> new UserNotFoundException("User not found with username: " + username));
        return convertToDTO(user);
    }

    public ReadUserDTO getUserById(Long userId) {
        UserEntity user = userRepository
                .findById(userId)
                .orElseThrow(() -> new UserNotFoundException("User not found with id: " + userId));
        return convertToDTO(user);
    }

    public List<ReadUserDTO> getAllUsers() {
        List<UserEntity> users = userRepository.findAll();
        return users.stream()
                .map(this::convertToDTO)
                .collect(Collectors.toList());
    }

    private ReadUserDTO convertToDTO(UserEntity user) {
        ReadUserDTO userDTO = new ReadUserDTO();
        userDTO.setUserId(user.getUserId());
        userDTO.setUsername(user.getUsername());
        userDTO.setEmail(user.getEmail());
        return userDTO;
    }
}