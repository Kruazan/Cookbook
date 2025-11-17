package com.cookbook.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Positive;
import jakarta.validation.constraints.Size;
import java.util.List;

public class RecipeCreateDTO {
    @NotBlank(message = "Название рецепта обязательно")
    @Size(min = 1, max = 100, message = "Название должно быть от 1 до 100 символов")
    private String name;

    @Positive(message = "Время приготовления должно быть положительным")
    private Integer cookingTime;

    @Positive(message = "Вес должен быть положительным")
    private Double weight;

    @Positive(message = "Калории должны быть положительными")
    private Integer calories;

    private List<StepCreateDTO> steps;

    // Геттеры и сеттеры
    public String getName() { return name; }
    public void setName(String name) { this.name = name; }
    public Integer getCookingTime() { return cookingTime; }
    public void setCookingTime(Integer cookingTime) { this.cookingTime = cookingTime; }
    public Double getWeight() { return weight; }
    public void setWeight(Double weight) { this.weight = weight; }
    public Integer getCalories() { return calories; }
    public void setCalories(Integer calories) { this.calories = calories; }
    public List<StepCreateDTO> getSteps() { return steps; }
    public void setSteps(List<StepCreateDTO> steps) { this.steps = steps; }
}