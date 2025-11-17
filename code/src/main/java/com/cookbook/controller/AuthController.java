package com.cookbook.controller;

import com.cookbook.dto.UserCreateDTO;
import com.cookbook.dto.UserResponseDTO;
import com.cookbook.model.User;
import com.cookbook.repository.UserRepository;
import com.cookbook.security.JwtUtil;
import com.cookbook.service.UserService;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.Map;

/**
 * Контроллер для авторизации и регистрации пользователей.
 */
@RestController
@RequestMapping("/api/auth")
public class AuthController {

    private final UserService userService;
    private final AuthenticationManager authenticationManager;
    private final JwtUtil jwtUtil;
    private final UserRepository userRepository;

    @Autowired
    public AuthController(UserService userService, AuthenticationManager authenticationManager,
                          JwtUtil jwtUtil, UserRepository userRepository) {
        this.userService = userService;
        this.authenticationManager = authenticationManager;
        this.jwtUtil = jwtUtil;
        this.userRepository = userRepository;
    }

    /**
     * Регистрация нового пользователя.
     * @param dto DTO с данными пользователя.
     * @return Ответ с данными созданного пользователя.
     */
    @PostMapping("/register")
    public ResponseEntity<UserResponseDTO> registerUser(@Valid @RequestBody UserCreateDTO dto) {
        UserResponseDTO response = userService.createUser(dto);
        return ResponseEntity.ok(response);
    }

    /**
     * Логин пользователя, возвращает JWT-токен.
     * @param request Мапа с username и password.
     * @return Ответ с JWT-токеном и userId.
     */
    @PostMapping("/login")
    public ResponseEntity<Map<String, Object>> loginUser(@RequestBody Map<String, String> request) {
        String username = request.get("username");
        String password = request.get("password");

        // Аутентификация пользователя
        Authentication authentication = authenticationManager.authenticate(
                new UsernamePasswordAuthenticationToken(username, password)
        );

        // Получение пользователя из репозитория для извлечения userId
        User user = userRepository.findByUsername(username)
                .orElseThrow(() -> new IllegalArgumentException("Пользователь с username " + username + " не найден"));

        // Генерация JWT-токена с правильным userId
        String token = jwtUtil.generateToken(username, user.getId());

        // Формирование ответа
        Map<String, Object> response = new HashMap<>();
        response.put("token", token);
        response.put("userId", user.getId());

        return ResponseEntity.ok(response);
    }
}