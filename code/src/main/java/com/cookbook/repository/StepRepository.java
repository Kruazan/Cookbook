package com.cookbook.repository;

import com.cookbook.model.Step;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface StepRepository extends JpaRepository<Step, Long> {
    List<Step> findByRecipeId(Long recipeId);
    List<Step> findByRecipeIdOrderByStepOrder(Long recipeId);
    Optional<Step> findByIdAndRecipeCreatedById(Long id, Long userId);
    void deleteByRecipeId(Long recipeId);
}