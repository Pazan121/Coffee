package com.coffemachine.coffemachine.service;

import com.coffemachine.coffemachine.dto.CartItem;
import com.coffemachine.coffemachine.entity.Drink;
import com.coffemachine.coffemachine.entity.Ingredient;
import com.coffemachine.coffemachine.repository.DrinkRepository;
import com.coffemachine.coffemachine.repository.IngredientRepository;
import jakarta.servlet.http.HttpSession;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service
public class CartService {

    private final DrinkRepository drinkRepository;
    private final IngredientRepository ingredientRepository;

    public CartService(DrinkRepository drinkRepository,
                       IngredientRepository ingredientRepository) {
        this.drinkRepository = drinkRepository;
        this.ingredientRepository = ingredientRepository;
    }

    @SuppressWarnings("unchecked")
    public List<CartItem> getCart(HttpSession session) {
        List<CartItem> cart = (List<CartItem>) session.getAttribute("cart");

        if (cart == null) {
            cart = new ArrayList<>();
            session.setAttribute("cart", cart);
        }

        return cart;
    }

    public String addToCart(Long drinkId,
                            Integer quantity,
                            List<Long> ingredientIds,
                            String pickupLocation,
                            HttpSession session) {

        Drink drink = drinkRepository.findById(drinkId).orElse(null);

        if (drink == null) {
            return "Напій не знайдено";
        }

        if (quantity == null || quantity <= 0) {
            return "Кількість напоїв має бути більшою за нуль";
        }

        if (pickupLocation == null || pickupLocation.isBlank()) {
            return "Потрібно вибрати місце отримання напою";
        }

        String ingredientsInfo = "Без додаткових складників";

        if (ingredientIds != null && !ingredientIds.isEmpty()) {
            StringBuilder builder = new StringBuilder();

            for (Long ingredientId : ingredientIds) {
                Ingredient ingredient = ingredientRepository.findById(ingredientId).orElse(null);

                if (ingredient == null) {
                    return "Один зі складників не знайдено";
                }

                if (!builder.isEmpty()) {
                    builder.append(", ");
                }

                builder.append(ingredient.getName());
            }

            ingredientsInfo = builder.toString();
        }

        CartItem item = new CartItem(
                drink.getId(),
                drink.getName(),
                quantity,
                drink.getPrice(),
                drink.getPrice() * quantity,
                ingredientIds,
                ingredientsInfo,
                pickupLocation
        );

        getCart(session).add(item);

        return "Позицію додано в кошик";
    }

    public void removeFromCart(Integer index, HttpSession session) {
        List<CartItem> cart = getCart(session);

        if (index != null && index >= 0 && index < cart.size()) {
            cart.remove((int) index);
        }
    }

    public void clearCart(HttpSession session) {
        session.removeAttribute("cart");
    }

    public Double getCartTotal(HttpSession session) {
        return getCart(session)
                .stream()
                .mapToDouble(CartItem::getTotalPrice)
                .sum();
    }
}