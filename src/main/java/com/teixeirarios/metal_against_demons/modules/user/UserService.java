package com.teixeirarios.metal_against_demons.modules.user;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
public class UserService {
    @Autowired
    private UserRepository userRepository;

    @Autowired
    private PasswordEncoder passwordEncoder;

    @Transactional
    public UserEntity createUser(String username, String email, String password) {
        UserEntity user = new UserEntity();
        user.setUsername(username);
        user.setEmail(email);
        user.setPasswordHash(passwordEncoder.encode(password));
        return userRepository.save(user);
    }

    @Transactional
    public UserEntity getUserByEmail(String email) {
        return userRepository.findByEmail(email);
    }

    @Transactional
    public UserEntity getUserById(Long userId) {
        return userRepository.findById(userId).orElse(null);
    }
}