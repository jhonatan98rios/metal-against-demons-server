package com.teixeirarios.metal_against_demons.modules.user;

import com.teixeirarios.metal_against_demons.modules.user.dtos.CreateUserDTO;
import com.teixeirarios.metal_against_demons.modules.user.dtos.LoginDTO;
import com.teixeirarios.metal_against_demons.modules.user.dtos.LoginResponseDTO;
import com.teixeirarios.metal_against_demons.modules.user.dtos.ReadUserDTO;
import com.teixeirarios.metal_against_demons.shared.security.TokenService;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/user")
public class UserController {
    @Autowired
    private UserService userService;

    @Autowired
    private TokenService tokenService;

    @Autowired
    private AuthenticationManager authenticationManager;

    @PostMapping()
    public ResponseEntity<ReadUserDTO> createUser(
            @Valid
            @RequestBody
            CreateUserDTO createUserDTO
    ) {
        ReadUserDTO user = userService.createUser(createUserDTO);
        return ResponseEntity.ok(user);
    }

    @GetMapping()
    public ResponseEntity<ReadUserDTO> readUser() {
        var authentication = SecurityContextHolder.getContext().getAuthentication();
        if (authentication == null) return ResponseEntity.notFound().build();

        UserEntity userEntity = (UserEntity) authentication.getPrincipal();
        if (userEntity == null) return ResponseEntity.notFound().build();

        ReadUserDTO readUserDTO = new ReadUserDTO();
        readUserDTO.setUserId(userEntity.getUserId());
        readUserDTO.setUsername(userEntity.getUsername());
        readUserDTO.setEmail(userEntity.getEmail());
        readUserDTO.setProfile(userEntity.getProfile());

        return ResponseEntity.ok(readUserDTO);
    }

    @GetMapping("/list")
    public ResponseEntity<List<ReadUserDTO>> listUsers() {
        List<ReadUserDTO> users = userService.getAllUsers();
        return ResponseEntity.ok(users);
    }

    @PostMapping("/login")
    public ResponseEntity login(@RequestBody @Valid LoginDTO loginDTO) {
        UsernamePasswordAuthenticationToken usernamePassword = new UsernamePasswordAuthenticationToken(
                loginDTO.getUsername(),
                loginDTO.getPassword()
        );

        Authentication auth = this.authenticationManager.authenticate(usernamePassword);
        String token = tokenService.generateToken( (UserEntity) auth.getPrincipal());
        return ResponseEntity.ok(new LoginResponseDTO(token));
    }

//    /* Avaliar se é necessário */
//    @GetMapping("/email/{email}")
//    public ResponseEntity<ReadUserDTO> getUserByEmail(@PathVariable String email) {
//        ReadUserDTO user = userService.getUserByEmail(email);
//        if (user == null) {
//            return ResponseEntity.notFound().build();
//        }
//        return ResponseEntity.ok(user);
//    }
//
//    /* Avaliar se é necessário */
//    @GetMapping("/username/{username}")
//    public ResponseEntity<ReadUserDTO> getUserByUsername(@PathVariable String username) {
//        ReadUserDTO user = userService.getUserByUsername(username);
//        if (user == null) {
//            return ResponseEntity.notFound().build();
//        }
//        return ResponseEntity.ok(user);
//    }
}