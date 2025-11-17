package com.cookbook.dto;

public class StepIngredientResponseDTO {
    private Long id;
    private Long ingredientId;
    private String ingredientName;
    private Double quantity;

    public StepIngredientResponseDTO(Long id, Long ingredientId, String ingredientName, Double quantity) {
        this.id = id;
        this.ingredientId = ingredientId;
        this.ingredientName = ingredientName;
        this.quantity = quantity;
    }

    // Геттеры и сеттеры
    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }
    public Long getIngredientId() { return ingredientId; }
    public void setIngredientId(Long ingredientId) { this.ingredientId = ingredientId; }
    public String getIngredientName() { return ingredientName; }
    public void setIngredientName(String ingredientName) { this.ingredientName = ingredientName; }
    public Double getQuantity() { return quantity; }
    public void setQuantity(Double quantity) { this.quantity = quantity; }
}