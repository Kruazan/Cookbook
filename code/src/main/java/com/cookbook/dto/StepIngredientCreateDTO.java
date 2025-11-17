package com.cookbook.dto;

import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;

public class StepIngredientCreateDTO {
    @NotNull(message = "ID ингредиента обязателен")
    private Long ingredientId;

    @Positive(message = "Количество должно быть положительным")
    private Double quantity;

    // Геттеры и сеттеры
    public Long getIngredientId() { return ingredientId; }
    public void setIngredientId(Long ingredientId) { this.ingredientId = ingredientId; }
    public Double getQuantity() { return quantity; }
    public void setQuantity(Double quantity) { this.quantity = quantity; }
}