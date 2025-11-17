package com.cookbook.dto;

import java.util.List;

public class RecipeResponseDTO {
    private Long id;
    private String name;
    private Integer cookingTime;
    private Double weight;
    private Integer calories;
    private List<StepResponseDTO> steps;
    private Long createdById;

    public RecipeResponseDTO(Long id, String name, Integer cookingTime, Double weight, Integer calories, List<StepResponseDTO> steps, Long createdById) {
        this.id = id;
        this.name = name;
        this.cookingTime = cookingTime;
        this.weight = weight;
        this.calories = calories;
        this.steps = steps;
        this.createdById = createdById;
    }

    // Геттеры и сеттеры
    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }
    public String getName() { return name; }
    public void setName(String name) { this.name = name; }
    public Integer getCookingTime() { return cookingTime; }
    public void setCookingTime(Integer cookingTime) { this.cookingTime = cookingTime; }
    public Double getWeight() { return weight; }
    public void setWeight(Double weight) { this.weight = weight; }
    public Integer getCalories() { return calories; }
    public void setCalories(Integer calories) { this.calories = calories; }
    public List<StepResponseDTO> getSteps() { return steps; }
    public void setSteps(List<StepResponseDTO> steps) { this.steps = steps; }
    public Long getCreatedById() { return createdById; }
    public void setCreatedById(Long createdById) { this.createdById = createdById; }
}