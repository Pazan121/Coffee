package com.coffemachine.coffemachine.service;

import com.coffemachine.coffemachine.entity.Drink;
import com.coffemachine.coffemachine.entity.Ingredient;
import com.coffemachine.coffemachine.entity.Order;
import com.coffemachine.coffemachine.entity.User;
import com.coffemachine.coffemachine.repository.DrinkRepository;
import com.coffemachine.coffemachine.repository.IngredientRepository;
import com.coffemachine.coffemachine.repository.OrderRepository;
import com.coffemachine.coffemachine.repository.UserRepository;
import com.coffemachine.coffemachine.dto.CartItem;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;

@Service
public class PurchaseService {

    private final UserRepository userRepository;
    private final DrinkRepository drinkRepository;
    private final OrderRepository orderRepository;
    private final IngredientRepository ingredientRepository;

    public PurchaseService(UserRepository userRepository,
                           DrinkRepository drinkRepository,
                           OrderRepository orderRepository,
                           IngredientRepository ingredientRepository) {
        this.userRepository = userRepository;
        this.drinkRepository = drinkRepository;
        this.orderRepository = orderRepository;
        this.ingredientRepository = ingredientRepository;
    }

    public String buyDrink(Long userId, Long drinkId, Integer quantity, List<Long> ingredientIds, String pickupLocation) {

        User user = userRepository.findById(userId).orElse(null);
        Drink drink = drinkRepository.findById(drinkId).orElse(null);

        if (user == null) {
            return "Користувача не знайдено";
        }

        if (drink == null) {
            return "Напій не знайдено";
        }

        if (quantity == null || quantity <= 0) {
            return "Кількість напоїв має бути більшою за нуль";
        }

        if (pickupLocation == null || pickupLocation.isBlank()) {
            return "Потрібно вибрати місце отримання напою";
        }

        if (drink.getPortions() == null || drink.getPortions() < quantity) {
            return "Недостатньо порцій напою";
        }

        double totalPrice = drink.getPrice() * quantity;

        if (user.getBalance() == null || user.getBalance() < totalPrice) {
            return "Недостатньо коштів на рахунку";
        }

        if (ingredientIds != null) {
            for (Long ingredientId : ingredientIds) {
                Ingredient ingredient = ingredientRepository.findById(ingredientId).orElse(null);

                if (ingredient == null) {
                    return "Один зі складників не знайдено";
                }

                if (ingredient.getAmount() == null || ingredient.getAmount() < quantity) {
                    return "Недостатньо складника \"" + ingredient.getName() + "\"";
                }
            }
        }

        user.setBalance(user.getBalance() - totalPrice);
        drink.setPortions(drink.getPortions() - quantity);

        userRepository.save(user);
        drinkRepository.save(drink);

        if (ingredientIds != null) {
            for (Long ingredientId : ingredientIds) {
                Ingredient ingredient = ingredientRepository.findById(ingredientId).orElse(null);

                if (ingredient != null) {
                    ingredient.setAmount(ingredient.getAmount() - quantity);
                    ingredientRepository.save(ingredient);
                }
            }
        }

        String ingredientsInfo = "Без додаткових складників";

        if (ingredientIds != null && !ingredientIds.isEmpty()) {
            StringBuilder builder = new StringBuilder();

            for (Long ingredientId : ingredientIds) {
                Ingredient ingredient = ingredientRepository.findById(ingredientId).orElse(null);

                if (ingredient != null) {
                    if (!builder.isEmpty()) {
                        builder.append(", ");
                    }

                    builder.append(ingredient.getName());
                }
            }

            ingredientsInfo = builder.toString();
        }

        Order order = new Order();
        order.setUser(user);
        order.setDrink(drink);
        order.setQuantity(quantity);
        order.setTotalPrice(totalPrice);
        order.setIngredientsInfo(ingredientsInfo);
        order.setPickupLocation(pickupLocation);
        order.setCreatedAt(LocalDateTime.now());

        orderRepository.save(order);

        return "Покупку успішно виконано";
    }

    public String checkoutCart(Long userId, List<CartItem> cart) {

        User user = userRepository.findById(userId).orElse(null);

        if (user == null) {
            return "Користувача не знайдено";
        }

        if (cart == null || cart.isEmpty()) {
            return "Кошик порожній";
        }

        double cartTotal = cart.stream()
                .mapToDouble(CartItem::getTotalPrice)
                .sum();

        if (user.getBalance() == null || user.getBalance() < cartTotal) {
            return "Недостатньо коштів на рахунку";
        }

        for (CartItem item : cart) {
            Drink drink = drinkRepository.findById(item.getDrinkId()).orElse(null);

            if (drink == null) {
                return "Один із напоїв не знайдено";
            }

            if (drink.getPortions() == null || drink.getPortions() < item.getQuantity()) {
                return "Недостатньо порцій напою \"" + drink.getName() + "\"";
            }

            if (item.getIngredientIds() != null) {
                for (Long ingredientId : item.getIngredientIds()) {
                    Ingredient ingredient = ingredientRepository.findById(ingredientId).orElse(null);

                    if (ingredient == null) {
                        return "Один зі складників не знайдено";
                    }

                    if (ingredient.getAmount() == null || ingredient.getAmount() < item.getQuantity()) {
                        return "Недостатньо складника \"" + ingredient.getName() + "\"";
                    }
                }
            }
        }

        user.setBalance(user.getBalance() - cartTotal);
        userRepository.save(user);

        for (CartItem item : cart) {
            Drink drink = drinkRepository.findById(item.getDrinkId()).orElse(null);

            if (drink == null) {
                continue;
            }

            drink.setPortions(drink.getPortions() - item.getQuantity());
            drinkRepository.save(drink);

            if (item.getIngredientIds() != null) {
                for (Long ingredientId : item.getIngredientIds()) {
                    Ingredient ingredient = ingredientRepository.findById(ingredientId).orElse(null);

                    if (ingredient != null) {
                        ingredient.setAmount(ingredient.getAmount() - item.getQuantity());
                        ingredientRepository.save(ingredient);
                    }
                }
            }

            Order order = new Order();
            order.setUser(user);
            order.setDrink(drink);
            order.setQuantity(item.getQuantity());
            order.setTotalPrice(item.getTotalPrice());
            order.setIngredientsInfo(item.getIngredientsInfo());
            order.setPickupLocation(item.getPickupLocation());
            order.setCreatedAt(LocalDateTime.now());

            orderRepository.save(order);
        }

        return "Покупку успішно оформлено";
    }
}