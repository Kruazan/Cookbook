package com.cookbook.mapper;

import com.cookbook.dto.*;
import com.cookbook.model.*;
import com.cookbook.model.Process;
import com.cookbook.repository.IngredientRepository;
import com.cookbook.repository.ProcessRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

@Component
public class CookbookMapper {

    private final ProcessRepository processRepository;
    private final IngredientRepository ingredientRepository;

    @Autowired
    public CookbookMapper(ProcessRepository processRepository, IngredientRepository ingredientRepository) {
        this.processRepository = processRepository;
        this.ingredientRepository = ingredientRepository;
    }

    // User
    public User toUser(UserCreateDTO dto) {
        User user = new User();
        user.setUsername(dto.getUsername());
        user.setPassword(dto.getPassword());
        user.setEmail(dto.getEmail());
        return user;
    }

    public UserResponseDTO toUserResponseDTO(User user) {
        return new UserResponseDTO(user.getId(), user.getUsername(), user.getEmail());
    }

    public void updateUser(User user, UserUpdateDTO dto) {
        if (dto.getUsername() != null) user.setUsername(dto.getUsername());
        if (dto.getPassword() != null) user.setPassword(dto.getPassword());
        if (dto.getEmail() != null) user.setEmail(dto.getEmail());
    }

    // Ingredient
    public Ingredient toIngredient(IngredientCreateDTO dto, User user) {
        Ingredient ingredient = new Ingredient();
        ingredient.setName(dto.getName());
        ingredient.setType(dto.getType());
        ingredient.setCreatedBy(user);
        return ingredient;
    }

    public IngredientResponseDTO toIngredientResponseDTO(Ingredient ingredient) {
        return new IngredientResponseDTO(
                ingredient.getId(),
                ingredient.getName(),
                ingredient.getType(),
                ingredient.getCreatedBy().getId()
        );
    }

    public void updateIngredient(Ingredient ingredient, IngredientUpdateDTO dto) {
        if (dto.getName() != null) ingredient.setName(dto.getName());
        if (dto.getType() != null) ingredient.setType(dto.getType());
    }

    // Process
    public Process toProcess(ProcessCreateDTO dto, User user) {
        Process process = new Process();
        process.setName(dto.getName());
        process.setNote(dto.getNote());
        process.setCreatedBy(user);
        return process;
    }

    public ProcessResponseDTO toProcessResponseDTO(Process process) {
        return new ProcessResponseDTO(
                process.getId(),
                process.getName(),
                process.getNote(),
                process.getCreatedBy().getId()
        );
    }

    public void updateProcess(Process process, ProcessUpdateDTO dto) {
        if (dto.getName() != null) process.setName(dto.getName());
        if (dto.getNote() != null) process.setNote(dto.getNote());
    }

    // StepIngredient
    public StepIngredient toStepIngredient(StepIngredientCreateDTO dto, Step step, Ingredient ingredient) {
        StepIngredient stepIngredient = new StepIngredient();
        stepIngredient.setStep(step);
        stepIngredient.setIngredient(ingredient);
        stepIngredient.setQuantity(dto.getQuantity());
        return stepIngredient;
    }

    public StepIngredientResponseDTO toStepIngredientResponseDTO(StepIngredient stepIngredient) {
        return new StepIngredientResponseDTO(
                stepIngredient.getId(),
                stepIngredient.getIngredient().getId(),
                stepIngredient.getIngredient().getName(),
                stepIngredient.getQuantity()
        );
    }

    // Step
    public Step toStep(StepCreateDTO dto, Recipe recipe, Process process, int stepOrder) {
        Step step = new Step();
        step.setRecipe(recipe);
        step.setStepOrder(stepOrder);
        step.setProcess(process);
        step.setDuration(dto.getDuration());
        step.setAdditionalNote(dto.getAdditionalNote());
        return step;
    }

    public StepResponseDTO toStepResponseDTO(Step step) {
        List<StepIngredientResponseDTO> stepIngredients = step.getStepIngredients().stream()
                .map(this::toStepIngredientResponseDTO)
                .collect(Collectors.toList());
        return new StepResponseDTO(
                step.getId(),
                step.getStepOrder(),
                step.getProcess().getId(),
                step.getProcess().getName(),
                step.getDuration(),
                step.getAdditionalNote(),
                stepIngredients
        );
    }

    public void updateStep(Step step, StepUpdateDTO dto, Process process) {
        step.setProcess(process);
        if (dto.getDuration() != null) step.setDuration(dto.getDuration());
        if (dto.getAdditionalNote() != null) step.setAdditionalNote(dto.getAdditionalNote());
    }

    // Recipe
    public Recipe toRecipe(RecipeCreateDTO dto, User user) {
        Recipe recipe = new Recipe();
        recipe.setName(dto.getName());
        recipe.setCookingTime(dto.getCookingTime());
        recipe.setWeight(dto.getWeight());
        recipe.setCalories(dto.getCalories());
        recipe.setCreatedBy(user);
        if (dto.getSteps() != null) {
            List<Step> steps = IntStream.range(0, dto.getSteps().size())
                    .mapToObj(i -> toStep(dto.getSteps().get(i), recipe, user.getId(), i + 1))
                    .collect(Collectors.toList());
            recipe.setSteps(steps);
        }
        return recipe;
    }

    public RecipeResponseDTO toRecipeResponseDTO(Recipe recipe) {
        List<StepResponseDTO> steps = recipe.getSteps().stream()
                .map(this::toStepResponseDTO)
                .collect(Collectors.toList());
        return new RecipeResponseDTO(
                recipe.getId(),
                recipe.getName(),
                recipe.getCookingTime(),
                recipe.getWeight(),
                recipe.getCalories(),
                steps,
                recipe.getCreatedBy().getId()
        );
    }

    public void updateRecipe(Recipe recipe, RecipeUpdateDTO dto) {
        if (dto.getName() != null) recipe.setName(dto.getName());
        if (dto.getCookingTime() != null) recipe.setCookingTime(dto.getCookingTime());
        if (dto.getWeight() != null) recipe.setWeight(dto.getWeight());
        if (dto.getCalories() != null) recipe.setCalories(dto.getCalories());
    }

    // Step
    private Step toStep(StepCreateDTO dto, Recipe recipe, Long userId, int stepOrder) {
        Step step = new Step();
        step.setRecipe(recipe);
        step.setStepOrder(stepOrder); // Устанавливаем stepOrder
        step.setProcess(processRepository.findById(dto.getProcessId())
                .orElseThrow(() -> new IllegalArgumentException("Процесс с ID " + dto.getProcessId() + " не найден")));
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

    private StepIngredient toStepIngredient(StepIngredientCreateDTO dto, Step step, Long userId) {
        StepIngredient stepIngredient = new StepIngredient();
        stepIngredient.setStep(step);
        stepIngredient.setIngredient(ingredientRepository.findById(dto.getIngredientId())
                .orElseThrow(() -> new IllegalArgumentException("Ингредиент с ID " + dto.getIngredientId() + " не найден")));
        stepIngredient.setQuantity(dto.getQuantity());
        return stepIngredient;
    }
}