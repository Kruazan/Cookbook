package com.cookbook.model;

import com.fasterxml.jackson.annotation.JsonBackReference;
import jakarta.persistence.*;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Positive;
import jakarta.validation.constraints.Size;
import java.util.ArrayList;
import java.util.List;

@Entity
@Table(name = "recipes")
public class Recipe {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @NotBlank
    @Size(min = 1, max = 100)
    @Column(nullable = false)
    private String name; // Название

    @Positive
    @Column(nullable = false)
    private Integer cookingTime; // Длительность приготовления (в минутах)

    @Positive
    @Column(nullable = false)
    private Double weight; // Вес блюда (в граммах)

    @Positive
    @Column(nullable = false)
    private Integer calories; // Ккал

    @OneToMany(mappedBy = "recipe", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<Step> steps = new ArrayList<>(); // Список шагов

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "created_by", nullable = false)
    @JsonBackReference
    private User createdBy; // Юзер, который создал

    // Конструкторы
    public Recipe() {}

    public Recipe(String name, Integer cookingTime, Double weight, Integer calories, User createdBy) {
        this.name = name;
        this.cookingTime = cookingTime;
        this.weight = weight;
        this.calories = calories;
        this.createdBy = createdBy;
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

    public List<Step> getSteps() { return steps; }
    public void setSteps(List<Step> steps) { this.steps = steps; }

    public User getCreatedBy() { return createdBy; }
    public void setCreatedBy(User createdBy) { this.createdBy = createdBy; }
}