package com.cookbook.dto;

import com.cookbook.model.IngredientType;

public class IngredientResponseDTO {
    private Long id;
    private String name;
    private IngredientType type;
    private Long createdById;

    public IngredientResponseDTO(Long id, String name, IngredientType type, Long createdById) {
        this.id = id;
        this.name = name;
        this.type = type;
        this.createdById = createdById;
    }

    // Геттеры и сеттеры
    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }
    public String getName() { return name; }
    public void setName(String name) { this.name = name; }
    public IngredientType getType() { return type; }
    public void setType(IngredientType type) { this.type = type; }
    public Long getCreatedById() { return createdById; }
    public void setCreatedById(Long createdById) { this.createdById = createdById; }
}