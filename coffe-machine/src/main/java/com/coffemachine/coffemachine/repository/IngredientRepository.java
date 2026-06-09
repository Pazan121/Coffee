package com.coffemachine.coffemachine.repository;

import com.coffemachine.coffemachine.entity.Ingredient;
import org.springframework.data.jpa.repository.JpaRepository;

public interface IngredientRepository extends JpaRepository<Ingredient, Long> {
}