package com.coffemachine.coffemachine.service.admin;

import com.coffemachine.coffemachine.entity.Drink;
import com.coffemachine.coffemachine.entity.Ingredient;
import com.coffemachine.coffemachine.entity.Order;
import com.coffemachine.coffemachine.entity.User;
import com.coffemachine.coffemachine.repository.DrinkRepository;
import com.coffemachine.coffemachine.repository.IngredientRepository;
import com.coffemachine.coffemachine.repository.OrderRepository;
import com.coffemachine.coffemachine.repository.UserRepository;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class AdminService {

    private final UserRepository userRepository;
    private final DrinkRepository drinkRepository;
    private final OrderRepository orderRepository;
    private final IngredientRepository ingredientRepository;

    public AdminService(UserRepository userRepository,
                        DrinkRepository drinkRepository,
                        OrderRepository orderRepository,
                        IngredientRepository ingredientRepository) {
        this.userRepository = userRepository;
        this.drinkRepository = drinkRepository;
        this.orderRepository = orderRepository;
        this.ingredientRepository = ingredientRepository;
    }

    public List<User> getAllUsers() {
        return userRepository.findAll();
    }

    public List<Drink> getAllDrinks() {
        return drinkRepository.findAll();
    }

    public List<Order> getAllOrders() {
        return orderRepository.findAll();
    }

    public List<Ingredient> getAllIngredients() {
        return ingredientRepository.findAll();
    }

    public void addDrink(String name, Double price, Integer portions, String imageName) {
        Drink drink = new Drink();
        drink.setName(name);
        drink.setPrice(price);
        drink.setPortions(portions);
        drink.setImageName(imageName);

        drinkRepository.save(drink);
    }

    public Drink getDrinkById(Long id) {
        return drinkRepository.findById(id).orElse(null);
    }

    public void updateDrink(Long id, String name, Double price, Integer portions, String imageName) {
        Drink drink = drinkRepository.findById(id).orElse(null);

        if (drink != null) {
            drink.setName(name);
            drink.setPrice(price);
            drink.setPortions(portions);
            drink.setImageName(imageName);
            drinkRepository.save(drink);
        }
    }

    public void refillDrink(Long id, Integer portions) {
        Drink drink = drinkRepository.findById(id).orElse(null);

        if (drink != null && portions != null && portions > 0) {
            drink.setPortions(drink.getPortions() + portions);
            drinkRepository.save(drink);
        }
    }

    public boolean deleteDrink(Long id) {
        Drink drink = drinkRepository.findById(id).orElse(null);

        if (drink == null) {
            return false;
        }

        if (orderRepository.existsByDrink(drink)) {
            return false;
        }

        drinkRepository.delete(drink);
        return true;
    }

    public void addIngredient(String name, Integer amount) {
        Ingredient ingredient = new Ingredient();
        ingredient.setName(name);
        ingredient.setAmount(amount);

        ingredientRepository.save(ingredient);
    }

    public void refillIngredient(Long id, Integer amount) {
        Ingredient ingredient = ingredientRepository.findById(id).orElse(null);

        if (ingredient != null && amount != null && amount > 0) {
            ingredient.setAmount(ingredient.getAmount() + amount);
            ingredientRepository.save(ingredient);
        }
    }

    public void deleteIngredient(Long id) {
        ingredientRepository.deleteById(id);
    }
}