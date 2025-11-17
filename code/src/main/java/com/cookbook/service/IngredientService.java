package com.cookbook.service;

import com.cookbook.dto.IngredientCreateDTO;
import com.cookbook.dto.IngredientResponseDTO;
import com.cookbook.dto.IngredientUpdateDTO;
import com.cookbook.mapper.CookbookMapper;
import com.cookbook.model.Ingredient;
import com.cookbook.model.User;
import com.cookbook.repository.IngredientRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class IngredientService {

    private final IngredientRepository ingredientRepository;
    private final CookbookMapper mapper;

    @Autowired
    public IngredientService(IngredientRepository ingredientRepository, CookbookMapper mapper) {
        this.ingredientRepository = ingredientRepository;
        this.mapper = mapper;
    }

    @Transactional
    public IngredientResponseDTO createIngredient(IngredientCreateDTO dto, User user) {
        if (ingredientRepository.existsByNameAndCreatedById(dto.getName(), user.getId())) {
            throw new IllegalArgumentException("Ингредиент с таким названием уже существует у пользователя");
        }
        Ingredient ingredient = mapper.toIngredient(dto, user);
        ingredient = ingredientRepository.save(ingredient);
        return mapper.toIngredientResponseDTO(ingredient);
    }

    public IngredientResponseDTO getIngredientById(Long id, Long userId) {
        Ingredient ingredient = ingredientRepository.findByIdAndCreatedById(id, userId)
                .orElseThrow(() -> new IllegalArgumentException("Ингредиент с ID " + id + " не найден для пользователя"));
        return mapper.toIngredientResponseDTO(ingredient);
    }

    public List<IngredientResponseDTO> getAllIngredientsByUser(Long userId) {
        return ingredientRepository.findByCreatedById(userId).stream()
                .map(mapper::toIngredientResponseDTO)
                .collect(Collectors.toList());
    }

    @Transactional
    public IngredientResponseDTO updateIngredient(Long id, Long userId, IngredientUpdateDTO dto) {
        Ingredient ingredient = ingredientRepository.findByIdAndCreatedById(id, userId)
                .orElseThrow(() -> new IllegalArgumentException("Ингредиент с ID " + id + " не найден для пользователя"));
        if (dto.getName() != null && !dto.getName().equals(ingredient.getName()) &&
                ingredientRepository.existsByNameAndCreatedById(dto.getName(), userId)) {
            throw new IllegalArgumentException("Ингредиент с таким названием уже существует у пользователя");
        }
        mapper.updateIngredient(ingredient, dto);
        ingredient = ingredientRepository.save(ingredient);
        return mapper.toIngredientResponseDTO(ingredient);
    }

    @Transactional
    public void deleteIngredient(Long id, Long userId) {
        Ingredient ingredient = ingredientRepository.findByIdAndCreatedById(id, userId)
                .orElseThrow(() -> new IllegalArgumentException("Ингредиент с ID " + id + " не найден для пользователя"));
        ingredientRepository.delete(ingredient);
    }
}