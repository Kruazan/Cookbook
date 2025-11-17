package com.cookbook.controller;

import com.cookbook.dto.UserResponseDTO;
import com.cookbook.dto.UserUpdateDTO;
import com.cookbook.service.UserService;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * Контроллер для управления пользователями.
 */
@RestController
@RequestMapping("/api/users")
public class UserController {

    private final UserService userService;

    @Autowired
    public UserController(UserService userService) {
        this.userService = userService;
    }

    /**
     * Получение текущего пользователя.
     * @return Данные пользователя из JWT-токена.
     */
    @GetMapping("/me")
    public ResponseEntity<UserResponseDTO> getCurrentUser() {
        Long userId = getCurrentUserId();
        UserResponseDTO user = userService.getUserById(userId);
        return ResponseEntity.ok(user);
    }

    /**
     * Получение всех пользователей (только для админов, если потребуется).
     * @return Список всех пользователей.
     */
    @GetMapping
    public ResponseEntity<List<UserResponseDTO>> getAllUsers() {
        List<UserResponseDTO> users = userService.getAllUsers();
        return ResponseEntity.ok(users);
    }

    /**
     * Обновление данных пользователя.
     * @param dto DTO с обновленными данными.
     * @return Обновленные данные пользователя.
     */
    @PutMapping("/me")
    public ResponseEntity<UserResponseDTO> updateUser(@Valid @RequestBody UserUpdateDTO dto) {
        Long userId = getCurrentUserId();
        UserResponseDTO updatedUser = userService.updateUser(userId, dto);
        return ResponseEntity.ok(updatedUser);
    }

    /**
     * Удаление текущего пользователя.
     */
    @DeleteMapping("/me")
    public ResponseEntity<Void> deleteUser() {
        Long userId = getCurrentUserId();
        userService.deleteUser(userId);
        return ResponseEntity.noContent().build();
    }

    /**
     * Извлечение userId из JWT-токена через SecurityContextHolder.
     * @return ID текущего пользователя.
     */
    private Long getCurrentUserId() {
        Object credentials = SecurityContextHolder.getContext().getAuthentication().getCredentials();
        if (credentials instanceof Long) {
            return (Long) credentials;
        }
        throw new IllegalStateException("Не удалось извлечь userId из токена");
    }
}