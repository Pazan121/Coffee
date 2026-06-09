package com.coffemachine.coffemachine.repository;

import com.coffemachine.coffemachine.entity.Drink;
import com.coffemachine.coffemachine.entity.Order;
import com.coffemachine.coffemachine.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface OrderRepository extends JpaRepository<Order, Long> {

    List<Order> findByUser(User user);

    boolean existsByDrink(Drink drink);

    long countByUser(User user);
}