package com.teixeirarios.metal_against_demons.modules.user;

import com.teixeirarios.metal_against_demons.modules.user.dtos.CreateUserDTO;
import com.teixeirarios.metal_against_demons.modules.user.dtos.ReadUserDTO;
import com.teixeirarios.metal_against_demons.modules.user.exceptions.*;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.security.crypto.password.PasswordEncoder;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

public class UserServiceTest {

    @Mock
    private UserRepository userRepository;

    @Mock
    private PasswordEncoder passwordEncoder;

    @InjectMocks
    private UserService userService;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    void testCreateUser() {
        CreateUserDTO createUserDTO = new CreateUserDTO();
        createUserDTO.setUsername("testuser");
        createUserDTO.setEmail("test@example.com");
        createUserDTO.setPassword("password");

        when(userRepository.findByEmail(anyString())).thenReturn(Optional.empty());
        when(userRepository.findByUsername(anyString())).thenReturn(Optional.empty());
        when(passwordEncoder.encode(anyString())).thenReturn("hashedPassword");
        when(userRepository.save(any(UserEntity.class))).thenAnswer(invocation -> invocation.getArgument(0));

        ReadUserDTO readUserDTO = userService.createUser(createUserDTO);

        assertNotNull(readUserDTO);
        assertEquals("testuser", readUserDTO.getUsername());
        assertEquals("test@example.com", readUserDTO.getEmail());
    }

    @Test
    void testCreateUserEmailAlreadyExists() {
        UserEntity user = new UserEntity();
        user.setEmail("test@example.com");
        user.setUsername("testuser");
        user.setPasswordHash("password");

        when(userRepository.findByEmail("test@example.com")).thenReturn(Optional.of(user));

        CreateUserDTO createUserDTO = new CreateUserDTO();
        createUserDTO.setEmail("test@example.com");
        createUserDTO.setUsername("testuser");
        createUserDTO.setPassword("password");

        assertThrows(EmailAlreadyExistsException.class, () -> userService.createUser(createUserDTO));
    }

    @Test
    void testCreateUserUsernameAlreadyExists() {
        UserEntity user = new UserEntity();
        user.setEmail("test@example.com");
        user.setUsername("testuser");
        user.setPasswordHash("password");

        when(userRepository.findByUsername("testuser")).thenReturn(Optional.of(user));

        CreateUserDTO createUserDTO = new CreateUserDTO();
        createUserDTO.setEmail("test@example.com");
        createUserDTO.setUsername("testuser");
        createUserDTO.setPassword("password");

        assertThrows(UsernameAlreadyExistsException.class, () -> userService.createUser(createUserDTO));
    }

    @Test
    void testGetUserByIdUser() {
        UserEntity user = new UserEntity();
        user.setUsername("testuser");
        user.setEmail("test@example.com");
        user.setPasswordHash("password");

        when(userRepository.findById(1L)).thenReturn(Optional.of(user));
        ReadUserDTO readUserDTO = userService.getUserById(1L);

        assertNotNull(readUserDTO);
        assertEquals("testuser", readUserDTO.getUsername());
        assertEquals("test@example.com", readUserDTO.getEmail());
    }

    @Test
    void testGetUserByIdUserNotFound() {
        when(userRepository.findById(anyLong())).thenReturn(Optional.empty());
        assertThrows(UserNotFoundException.class, () -> userService.getUserById(0L));
    }

    @Test
    void testGetUserByEmailUser() {
        UserEntity user = new UserEntity();
        user.setUsername("testuser");
        user.setEmail("test@example.com");
        user.setPasswordHash("password");

        when(userRepository.findByEmail("test@example.com")).thenReturn(Optional.of(user));
        ReadUserDTO readUserDTO = userService.getUserByEmail("test@example.com");

        assertNotNull(readUserDTO);
        assertEquals("testuser", readUserDTO.getUsername());
        assertEquals("test@example.com", readUserDTO.getEmail());
    }

    @Test
    void testGetUserByEmailUserNotFound() {
        when(userRepository.findByEmail(anyString())).thenReturn(Optional.empty());
        assertThrows(UserNotFoundException.class, () -> userService.getUserByEmail("nonexistent@example.com"));
    }

    @Test
    void testGetUserByUsername() {
        UserEntity user = new UserEntity();
        user.setUsername("testuser");
        user.setEmail("test@example.com");
        user.setPasswordHash("password");

        when(userRepository.findByUsername("testuser")).thenReturn(Optional.of(user));
        ReadUserDTO readUserDTO = userService.getUserByUsername("testuser");

        assertNotNull(readUserDTO);
        assertEquals("testuser", readUserDTO.getUsername());
        assertEquals("test@example.com", readUserDTO.getEmail());
    }

    @Test
    void testGetUserByUsernameUserNotFound() {
        when(userRepository.findByUsername(anyString())).thenReturn(Optional.empty());
        assertThrows(UserNotFoundException.class, () -> userService.getUserByUsername("nonexistent"));
    }


}
