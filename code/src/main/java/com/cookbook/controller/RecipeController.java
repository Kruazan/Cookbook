package com.cookbook.controller;

import com.cookbook.dto.RecipeCreateDTO;
import com.cookbook.dto.RecipeResponseDTO;
import com.cookbook.dto.RecipeUpdateDTO;
import com.cookbook.model.User;
import com.cookbook.repository.UserRepository;
import com.cookbook.service.RecipeService;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * Контроллер для управления рецептами.
 */
@RestController
@RequestMapping("/api/recipes")
public class RecipeController {

    private final RecipeService recipeService;
    private final UserRepository userRepository;

    @Autowired
    public RecipeController(RecipeService recipeService, UserRepository userRepository) {
        this.recipeService = recipeService;
        this.userRepository = userRepository;
    }

    /**
     * Создание нового рецепта.
     * @param dto DTO для создания рецепта.
     * @return Данные созданного рецепта.
     */
    @PostMapping
    public ResponseEntity<RecipeResponseDTO> createRecipe(@Valid @RequestBody RecipeCreateDTO dto) {
        User user = getCurrentUser();
        RecipeResponseDTO recipe = recipeService.createRecipe(dto, user);
        return ResponseEntity.ok(recipe);
    }

    /**
     * Получение рецепта по ID.
     * @param id ID рецепта.
     * @return Данные рецепта.
     */
    @GetMapping("/{id}")
    public ResponseEntity<RecipeResponseDTO> getRecipe(@PathVariable Long id) {
        Long userId = getCurrentUserId();
        RecipeResponseDTO recipe = recipeService.getRecipeById(id, userId);
        return ResponseEntity.ok(recipe);
    }

    /**
     * Получение всех рецептов пользователя.
     * @return Список рецептов.
     */
    @GetMapping
    public ResponseEntity<List<RecipeResponseDTO>> getAllRecipes() {
        Long userId = getCurrentUserId();
        List<RecipeResponseDTO> recipes = recipeService.getAllRecipesByUser(userId);
        return ResponseEntity.ok(recipes);
    }

    /**
     * Обновление рецепта.
     * @param id ID рецепта.
     * @param dto DTO с обновленными данными.
     * @return Обновленные данные рецепта.
     */
    @PutMapping("/{id}")
    public ResponseEntity<RecipeResponseDTO> updateRecipe(@PathVariable Long id, @Valid @RequestBody RecipeUpdateDTO dto) {
        Long userId = getCurrentUserId();
        RecipeResponseDTO updatedRecipe = recipeService.updateRecipe(id, userId, dto);
        return ResponseEntity.ok(updatedRecipe);
    }

    /**
     * Удаление рецепта.
     * @param id ID рецепта.
     */
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteRecipe(@PathVariable Long id) {
        Long userId = getCurrentUserId();
        recipeService.deleteRecipe(id, userId);
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