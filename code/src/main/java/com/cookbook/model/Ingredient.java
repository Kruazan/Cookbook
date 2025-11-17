package com.cookbook.model;

import com.fasterxml.jackson.annotation.JsonBackReference;
import jakarta.persistence.*;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;

@Entity
@Table(name = "ingredients")
public class Ingredient {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @NotBlank
    @Size(min = 1, max = 100)
    @Column(nullable = false)
    private String name; // Название

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private IngredientType type;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "created_by", nullable = false)
    @JsonBackReference
    private User createdBy; // Юзер, который создал

    // Конструкторы
    public Ingredient() {}

    public Ingredient(String name, IngredientType type, User createdBy) {
        this.name = name;
        this.type = type;
        this.createdBy = createdBy;
    }

    // Геттеры и сеттеры
    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }

    public String getName() { return name; }
    public void setName(String name) { this.name = name; }

    public IngredientType getType() { return type; }
    public void setType(IngredientType type) { this.type = type; }

    public User getCreatedBy() { return createdBy; }
    public void setCreatedBy(User createdBy) { this.createdBy = createdBy; }
}