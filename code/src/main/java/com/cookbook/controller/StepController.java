package com.cookbook.controller;

import com.cookbook.dto.StepCreateDTO;
import com.cookbook.dto.StepResponseDTO;
import com.cookbook.dto.StepUpdateDTO;
import com.cookbook.model.User;
import com.cookbook.repository.UserRepository;
import com.cookbook.service.StepService;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * Контроллер для управления шагами рецепта.
 */
@RestController
@RequestMapping("/api/steps")
public class StepController {

    private final StepService stepService;
    private final UserRepository userRepository;

    @Autowired
    public StepController(StepService stepService, UserRepository userRepository) {
        this.stepService = stepService;
        this.userRepository = userRepository;
    }

    /**
     * Получение шага по ID.
     * @param id ID шага.
     * @return Данные шага.
     */
    @GetMapping("/{id}")
    public ResponseEntity<StepResponseDTO> getStep(@PathVariable Long id) {
        StepResponseDTO step = stepService.getStepById(id);
        return ResponseEntity.ok(step);
    }

    /**
     * Получение всех шагов рецепта.
     * @param recipeId ID рецепта.
     * @return Список шагов.
     */
    @GetMapping("/recipe/{recipeId}")
    public ResponseEntity<List<StepResponseDTO>> getStepsByRecipe(@PathVariable Long recipeId) {
        List<StepResponseDTO> steps = stepService.getStepsByRecipeId(recipeId);
        return ResponseEntity.ok(steps);
    }

    /**
     * Обновление шага.
     * @param id ID шага.
     * @param dto DTO с обновленными данными.
     * @return Обновленные данные шага.
     */
    @PutMapping("/{id}")
    public ResponseEntity<StepResponseDTO> updateStep(@PathVariable Long id, @Valid @RequestBody StepUpdateDTO dto) {
        Long userId = getCurrentUserId();
        StepResponseDTO updatedStep = stepService.updateStep(id, userId, dto);
        return ResponseEntity.ok(updatedStep);
    }

    /**
     * Удаление шага.
     * @param id ID шага.
     */
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteStep(@PathVariable Long id) {
        stepService.deleteStep(id);
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