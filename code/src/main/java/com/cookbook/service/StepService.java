package com.cookbook.service;

import com.cookbook.dto.StepCreateDTO;
import com.cookbook.dto.StepIngredientCreateDTO;
import com.cookbook.dto.StepResponseDTO;
import com.cookbook.dto.StepUpdateDTO;
import com.cookbook.mapper.CookbookMapper;
import com.cookbook.model.*;
import com.cookbook.model.Process;
import com.cookbook.repository.IngredientRepository;
import com.cookbook.repository.ProcessRepository;
import com.cookbook.repository.StepIngredientRepository;
import com.cookbook.repository.StepRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class StepService {

    private final StepRepository stepRepository;
    private final StepIngredientRepository stepIngredientRepository;
    private final ProcessRepository processRepository;
    private final IngredientRepository ingredientRepository;
    private final CookbookMapper mapper;

    @Autowired
    public StepService(StepRepository stepRepository, StepIngredientRepository stepIngredientRepository,
                       ProcessRepository processRepository, IngredientRepository ingredientRepository,
                       CookbookMapper mapper) {
        this.stepRepository = stepRepository;
        this.stepIngredientRepository = stepIngredientRepository;
        this.processRepository = processRepository;
        this.ingredientRepository = ingredientRepository;
        this.mapper = mapper;
    }

    @Transactional
    public StepResponseDTO createStep(StepCreateDTO dto, Recipe recipe, int stepOrder, Long userId) {
        Process process = processRepository.findByIdAndCreatedById(dto.getProcessId(), userId)
                .orElseThrow(() -> new IllegalArgumentException("Процесс с ID " + dto.getProcessId() + " не найден для пользователя"));
        Step step = mapper.toStep(dto, recipe, process, stepOrder); // Передаем stepOrder
        if (dto.getStepIngredients() != null) {
            for (StepIngredientCreateDTO siDto : dto.getStepIngredients()) {
                Ingredient ingredient = ingredientRepository.findByIdAndCreatedById(siDto.getIngredientId(), userId)
                        .orElseThrow(() -> new IllegalArgumentException("Ингредиент с ID " + siDto.getIngredientId() + " не найден для пользователя"));
                StepIngredient stepIngredient = mapper.toStepIngredient(siDto, step, ingredient);
                step.getStepIngredients().add(stepIngredient);
            }
        }
        step = stepRepository.save(step);
        return mapper.toStepResponseDTO(step);
    }

    private StepIngredient toStepIngredient(StepIngredientCreateDTO dto, Step step, Long userId) {
        Ingredient ingredient = ingredientRepository.findById(dto.getIngredientId())
                .orElseThrow(() -> new IllegalArgumentException("Ингредиент с ID " + dto.getIngredientId() + " не найден"));
        StepIngredient stepIngredient = new StepIngredient();
        stepIngredient.setStep(step);
        stepIngredient.setIngredient(ingredient);
        stepIngredient.setQuantity(dto.getQuantity());
        return stepIngredient;
    }

    public Step toStep(StepCreateDTO dto, Recipe recipe, int stepOrder, Long userId) {
        Process process = processRepository.findById(dto.getProcessId())
                .orElseThrow(() -> new IllegalArgumentException("Процесс с ID " + dto.getProcessId() + " не найден"));
        Step step = new Step();
        step.setRecipe(recipe);
        step.setProcess(process);
        step.setDuration(dto.getDuration());
        step.setAdditionalNote(dto.getAdditionalNote());
        if (dto.getStepIngredients() != null) {
            List<StepIngredient> stepIngredients = dto.getStepIngredients().stream()
                    .map(siDto -> toStepIngredient(siDto, step, userId))
                    .collect(Collectors.toList());
            step.setStepIngredients(stepIngredients);
        }
        return step;
    }

    public StepResponseDTO getStepById(Long id) {
        Step step = stepRepository.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("Шаг с ID " + id + " не найден"));
        return mapper.toStepResponseDTO(step);
    }

    public List<StepResponseDTO> getStepsByRecipeId(Long recipeId) {
        return stepRepository.findByRecipeId(recipeId).stream()
                .map(mapper::toStepResponseDTO)
                .collect(Collectors.toList());
    }

    @Transactional
    public StepResponseDTO updateStep(Long id, Long userId, StepUpdateDTO dto) {
        Step step = stepRepository.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("Шаг с ID " + id + " не найден"));
        Process process = processRepository.findByIdAndCreatedById(dto.getProcessId(), userId)
                .orElseThrow(() -> new IllegalArgumentException("Процесс с ID " + dto.getProcessId() + " не найден для пользователя"));
        step.getStepIngredients().clear();
        if (dto.getStepIngredients() != null) {
            for (StepIngredientCreateDTO siDto : dto.getStepIngredients()) {
                Ingredient ingredient = ingredientRepository.findByIdAndCreatedById(siDto.getIngredientId(), userId)
                        .orElseThrow(() -> new IllegalArgumentException("Ингредиент с ID " + siDto.getIngredientId() + " не найден для пользователя"));
                StepIngredient stepIngredient = mapper.toStepIngredient(siDto, step, ingredient);
                step.getStepIngredients().add(stepIngredient);
            }
        }
        mapper.updateStep(step, dto, process);
        step = stepRepository.save(step);
        return mapper.toStepResponseDTO(step);
    }

    @Transactional
    public void deleteStep(Long id) {
        Step step = stepRepository.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("Шаг с ID " + id + " не найден"));
        stepRepository.delete(step);
    }
}