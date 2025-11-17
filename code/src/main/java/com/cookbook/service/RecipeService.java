package com.cookbook.service;

import com.cookbook.dto.RecipeCreateDTO;
import com.cookbook.dto.RecipeResponseDTO;
import com.cookbook.dto.RecipeUpdateDTO;
import com.cookbook.mapper.CookbookMapper;
import com.cookbook.model.Recipe;
import com.cookbook.model.User;
import com.cookbook.repository.RecipeRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class RecipeService {

    private final RecipeRepository recipeRepository;
    private final StepService stepService;
    private final CookbookMapper mapper;

    @Autowired
    public RecipeService(RecipeRepository recipeRepository, StepService stepService, CookbookMapper mapper) {
        this.recipeRepository = recipeRepository;
        this.stepService = stepService;
        this.mapper = mapper;
    }

    @Transactional
    public RecipeResponseDTO createRecipe(RecipeCreateDTO dto, User user) {
        if (recipeRepository.existsByNameAndCreatedById(dto.getName(), user.getId())) {
            throw new IllegalArgumentException("Рецепт с таким названием уже существует у пользователя");
        }
        Recipe recipe = mapper.toRecipe(dto, user);
        recipe = recipeRepository.save(recipe); // Каскадное сохранение шагов
        return mapper.toRecipeResponseDTO(recipe);
    }

    public RecipeResponseDTO getRecipeById(Long id, Long userId) {
        Recipe recipe = recipeRepository.findByIdAndCreatedById(id, userId)
                .orElseThrow(() -> new IllegalArgumentException("Рецепт с ID " + id + " не найден для пользователя"));
        return mapper.toRecipeResponseDTO(recipe);
    }

    public List<RecipeResponseDTO> getAllRecipesByUser(Long userId) {
        return recipeRepository.findByCreatedById(userId).stream()
                .map(mapper::toRecipeResponseDTO)
                .collect(Collectors.toList());
    }

    @Transactional
    public RecipeResponseDTO updateRecipe(Long id, Long userId, RecipeUpdateDTO dto) {
        Recipe recipe = recipeRepository.findByIdAndCreatedById(id, userId)
                .orElseThrow(() -> new IllegalArgumentException("Рецепт с ID " + id + " не найден для пользователя"));
        if (dto.getName() != null && !dto.getName().equals(recipe.getName()) &&
                recipeRepository.existsByNameAndCreatedById(dto.getName(), userId)) {
            throw new IllegalArgumentException("Рецепт с таким названием уже существует у пользователя");
        }
        mapper.updateRecipe(recipe, dto);
        if (dto.getSteps() != null) {
            recipe.getSteps().clear();
            for (int i = 0; i < dto.getSteps().size(); i++) {
                stepService.createStep(dto.getSteps().get(i), recipe, i + 1, userId);
            }
        }
        recipe = recipeRepository.save(recipe);
        return mapper.toRecipeResponseDTO(recipe);
    }

    @Transactional
    public void deleteRecipe(Long id, Long userId) {
        Recipe recipe = recipeRepository.findByIdAndCreatedById(id, userId)
                .orElseThrow(() -> new IllegalArgumentException("Рецепт с ID " + id + " не найден для пользователя"));
        recipeRepository.delete(recipe);
    }
}