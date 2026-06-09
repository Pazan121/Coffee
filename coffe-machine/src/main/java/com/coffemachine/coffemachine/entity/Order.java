package com.coffemachine.coffemachine.entity;

import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDateTime;

@Entity
@Table(name = "orders")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class Order {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne
    private User user;

    @ManyToOne
    private Drink drink;

    private Integer quantity;
    private Double totalPrice;
    private String ingredientsInfo;
    private String pickupLocation;
    private LocalDateTime createdAt;
}