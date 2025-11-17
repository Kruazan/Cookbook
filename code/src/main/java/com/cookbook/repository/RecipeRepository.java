package com.cookbook.repository;

import com.cookbook.model.Recipe;
import com.cookbook.model.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface RecipeRepository extends JpaRepository<Recipe, Long> {
    List<Recipe> findByCreatedBy(User createdBy);
    List<Recipe> findByCreatedById(Long userId);
    Optional<Recipe> findByIdAndCreatedById(Long id, Long userId);
    boolean existsByNameAndCreatedById(String name, Long userId);
}