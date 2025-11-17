package com.cookbook.dto;

import com.cookbook.model.IngredientType;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;

public class IngredientCreateDTO {
    @NotBlank(message = "Название ингредиента обязательно")
    @Size(min = 1, max = 100, message = "Название должно быть от 1 до 100 символов")
    private String name;

    @NotNull(message = "Тип ингредиента обязателен")
    private IngredientType type;

    // Геттеры и сеттеры
    public String getName() { return name; }
    public void setName(String name) { this.name = name; }
    public IngredientType getType() { return type; }
    public void setType(IngredientType type) { this.type = type; }
}