package com.cookbook.repository;

import com.cookbook.model.Ingredient;
import com.cookbook.model.IngredientType;
import com.cookbook.model.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface IngredientRepository extends JpaRepository<Ingredient, Long> {
    List<Ingredient> findByCreatedBy(User createdBy);
    List<Ingredient> findByCreatedById(Long userId);
    Optional<Ingredient> findByIdAndCreatedById(Long id, Long userId);
    List<Ingredient> findByType(IngredientType type);
    boolean existsByNameAndCreatedById(String name, Long userId);
}