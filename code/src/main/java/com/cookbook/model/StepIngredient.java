package com.cookbook.model;

import jakarta.persistence.*;
import jakarta.validation.constraints.Positive;

@Entity
@Table(name = "step_ingredients")
public class StepIngredient {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "step_id", nullable = false)
    private Step step; // Шаг, к которому относится ингредиент

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "ingredient_id", nullable = false)
    private Ingredient ingredient; // Ингредиент

    @Positive
    @Column(nullable = false)
    private Double quantity; // Количество (например, в граммах, мл и т.д.)

    // Конструкторы
    public StepIngredient() {}

    public StepIngredient(Step step, Ingredient ingredient, Double quantity) {
        this.step = step;
        this.ingredient = ingredient;
        this.quantity = quantity;
    }

    // Геттеры и сеттеры
    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }

    public Step getStep() { return step; }
    public void setStep(Step step) { this.step = step; }

    public Ingredient getIngredient() { return ingredient; }
    public void setIngredient(Ingredient ingredient) { this.ingredient = ingredient; }

    public Double getQuantity() { return quantity; }
    public void setQuantity(Double quantity) { this.quantity = quantity; }
}