package com.cookbook.model;

import com.fasterxml.jackson.annotation.JsonManagedReference;
import jakarta.persistence.*;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import java.util.HashSet;
import java.util.Set;

@Entity
@Table(name = "users")
public class User {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @NotBlank
    @Size(min = 3, max = 50)
    @Column(unique = true, nullable = false)
    private String username; // Логин

    @NotBlank
    @Size(min = 6)
    @Column(nullable = false)
    private String password; // Пароль

    @Email
    @NotBlank
    @Column(unique = true, nullable = false)
    private String email; // Почта

    @OneToMany(mappedBy = "createdBy", cascade = CascadeType.ALL, orphanRemoval = true)
    @JsonManagedReference
    private Set<Ingredient> ingredients = new HashSet<>();

    @OneToMany(mappedBy = "createdBy", cascade = CascadeType.ALL, orphanRemoval = true)
    @JsonManagedReference
    private Set<Process> processes = new HashSet<>();

    @OneToMany(mappedBy = "createdBy", cascade = CascadeType.ALL, orphanRemoval = true)
    @JsonManagedReference
    private Set<Recipe> recipes = new HashSet<>();

    // Конструкторы
    public User() {}

    public User(String username, String password, String email) {
        this.username = username;
        this.password = password;
        this.email = email;
    }

    // Геттеры и сеттеры
    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }

    public String getUsername() { return username; }
    public void setUsername(String username) { this.username = username; }

    public String getPassword() { return password; }
    public void setPassword(String password) { this.password = password; }

    public String getEmail() { return email; }
    public void setEmail(String email) { this.email = email; }

    public Set<Ingredient> getIngredients() { return ingredients; }
    public void setIngredients(Set<Ingredient> ingredients) { this.ingredients = ingredients; }

    public Set<Process> getProcesses() { return processes; }
    public void setProcesses(Set<Process> processes) { this.processes = processes; }

    public Set<Recipe> getRecipes() { return recipes; }
    public void setRecipes(Set<Recipe> recipes) { this.recipes = recipes; }
}