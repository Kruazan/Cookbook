package com.cookbook.controller;

import com.cookbook.dto.IngredientCreateDTO;
import com.cookbook.dto.IngredientResponseDTO;
import com.cookbook.dto.IngredientUpdateDTO;
import com.cookbook.model.User;
import com.cookbook.repository.UserRepository;
import com.cookbook.service.IngredientService;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * Контроллер для управления ингредиентами.
 */
@RestController
@RequestMapping("/api/ingredients")
public class IngredientController {

    private final IngredientService ingredientService;
    private final UserRepository userRepository;

    @Autowired
    public IngredientController(IngredientService ingredientService, UserRepository userRepository) {
        this.ingredientService = ingredientService;
        this.userRepository = userRepository;
    }

    /**
     * Создание нового ингредиента.
     * @param dto DTO для создания ингредиента.
     * @return Данные созданного ингредиента.
     */
    @PostMapping
    public ResponseEntity<IngredientResponseDTO> createIngredient(@Valid @RequestBody IngredientCreateDTO dto) {
        User user = getCurrentUser();
        IngredientResponseDTO ingredient = ingredientService.createIngredient(dto, user);
        return ResponseEntity.ok(ingredient);
    }

    /**
     * Получение ингредиента по ID.
     * @param id ID ингредиента.
     * @return Данные ингредиента.
     */
    @GetMapping("/{id}")
    public ResponseEntity<IngredientResponseDTO> getIngredient(@PathVariable Long id) {
        Long userId = getCurrentUserId();
        IngredientResponseDTO ingredient = ingredientService.getIngredientById(id, userId);
        return ResponseEntity.ok(ingredient);
    }

    /**
     * Получение всех ингредиентов пользователя.
     * @return Список ингредиентов.
     */
    @GetMapping
    public ResponseEntity<List<IngredientResponseDTO>> getAllIngredients() {
        Long userId = getCurrentUserId();
        List<IngredientResponseDTO> ingredients = ingredientService.getAllIngredientsByUser(userId);
        return ResponseEntity.ok(ingredients);
    }

    /**
     * Обновление ингредиента.
     * @param id ID ингредиента.
     * @param dto DTO с обновленными данными.
     * @return Обновленные данные ингредиента.
     */
    @PutMapping("/{id}")
    public ResponseEntity<IngredientResponseDTO> updateIngredient(@PathVariable Long id, @Valid @RequestBody IngredientUpdateDTO dto) {
        Long userId = getCurrentUserId();
        IngredientResponseDTO updatedIngredient = ingredientService.updateIngredient(id, userId, dto);
        return ResponseEntity.ok(updatedIngredient);
    }

    /**
     * Удаление ингредиента.
     * @param id ID ингредиента.
     */
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteIngredient(@PathVariable Long id) {
        Long userId = getCurrentUserId();
        ingredientService.deleteIngredient(id, userId);
        return ResponseEntity.noContent().build();
    }

    /**
     * Извлечение текущего пользователя из базы данных.
     * @return Объект User.
     */
    private User getCurrentUser() {
        Long userId = getCurrentUserId();
        return userRepository.findById(userId)
                .orElseThrow(() -> new IllegalArgumentException("Пользователь с ID " + userId + " не найден"));
    }

    /**
     * Извлечение userId из JWT-токена.
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