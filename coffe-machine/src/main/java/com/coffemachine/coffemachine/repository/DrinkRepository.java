package com.coffemachine.coffemachine.repository;

import com.coffemachine.coffemachine.entity.Drink;
import org.springframework.data.jpa.repository.JpaRepository;

public interface DrinkRepository extends JpaRepository<Drink, Long> {
}