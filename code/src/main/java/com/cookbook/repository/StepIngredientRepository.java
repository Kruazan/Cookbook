package com.cookbook.repository;

import com.cookbook.model.StepIngredient;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface StepIngredientRepository extends JpaRepository<StepIngredient, Long> {
    List<StepIngredient> findByStepId(Long stepId);
}