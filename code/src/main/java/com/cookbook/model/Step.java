package com.cookbook.model;

import jakarta.persistence.*;
import java.util.ArrayList;
import java.util.List;

@Entity
@Table(name = "steps")
public class Step {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "step_order", nullable = false)
    private Integer stepOrder;

    private Integer duration;

    @Column(name = "additional_note")
    private String additionalNote;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "recipe_id", nullable = false)
    private Recipe recipe;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "process_id", nullable = false)
    private Process process;

    @OneToMany(mappedBy = "step", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<StepIngredient> stepIngredients = new ArrayList<>();

    // Геттеры и сеттеры
    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }
    public Integer getStepOrder() { return stepOrder; }
    public void setStepOrder(Integer stepOrder) { this.stepOrder = stepOrder; }
    public Integer getDuration() { return duration; }
    public void setDuration(Integer duration) { this.duration = duration; }
    public String getAdditionalNote() { return additionalNote; }
    public void setAdditionalNote(String additionalNote) { this.additionalNote = additionalNote; }
    public Recipe getRecipe() { return recipe; }
    public void setRecipe(Recipe recipe) { this.recipe = recipe; }
    public Process getProcess() { return process; }
    public void setProcess(Process process) { this.process = process; }
    public List<StepIngredient> getStepIngredients() { return stepIngredients; }
    public void setStepIngredients(List<StepIngredient> stepIngredients) { this.stepIngredients = stepIngredients; }
}