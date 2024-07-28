package com.teixeirarios.metal_against_demons.modules.user;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/users")
public class UserController {
    @Autowired
    private UserService userService;

    @PostMapping
    public ResponseEntity<ReadUserDTO> createUser(@RequestBody CreateUserDTO createUserDTO) {
        ReadUserDTO user = userService.createUser(createUserDTO.getUsername(), createUserDTO.getEmail(), createUserDTO.getPassword());
        return ResponseEntity.ok(user);
    }

    @GetMapping("/{id}")
    public ResponseEntity<ReadUserDTO> getUserById(@PathVariable Long id) {
        ReadUserDTO user = userService.getUserById(id);
        if (user == null) {
            return ResponseEntity.notFound().build();
        }
        return ResponseEntity.ok(user);
    }

    @GetMapping("/email/{email}")
    public ResponseEntity<ReadUserDTO> getUserByEmail(@PathVariable String email) {
        ReadUserDTO user = userService.getUserByEmail(email);
        if (user == null) {
            return ResponseEntity.notFound().build();
        }
        return ResponseEntity.ok(user);
    }

    @GetMapping
    public ResponseEntity<List<ReadUserDTO>> getAllUsers() {
        List<ReadUserDTO> users = userService.getAllUsers();
        return ResponseEntity.ok(users);
    }
}