package com.coffemachine.coffemachine.config;

import com.coffemachine.coffemachine.entity.Drink;
import com.coffemachine.coffemachine.entity.Ingredient;
import com.coffemachine.coffemachine.entity.User;
import com.coffemachine.coffemachine.repository.DrinkRepository;
import com.coffemachine.coffemachine.repository.IngredientRepository;
import com.coffemachine.coffemachine.repository.UserRepository;
import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Component;

@Component
public class DataInitializer implements CommandLineRunner {

    private final UserRepository userRepository;
    private final DrinkRepository drinkRepository;
    private final IngredientRepository ingredientRepository;

    public DataInitializer(UserRepository userRepository,
                           DrinkRepository drinkRepository,
                           IngredientRepository ingredientRepository) {
        this.userRepository = userRepository;
        this.drinkRepository = drinkRepository;
        this.ingredientRepository = ingredientRepository;
    }

    @Override
    public void run(String... args) {

        if (userRepository.count() == 0) {
            User admin = new User();
            admin.setName("admin");
            admin.setPassword("admin");
            admin.setBalance(0.0);
            admin.setRole("ADMIN");

            User user = new User();
            user.setName("user");
            user.setPassword("user");
            user.setBalance(500.0);
            user.setRole("USER");

            userRepository.save(admin);
            userRepository.save(user);
        }

        if (drinkRepository.count() == 0) {
            Drink espresso = new Drink();
            espresso.setName("Еспресо");
            espresso.setPrice(35.0);
            espresso.setPortions(20);
            espresso.setImageName("espresso.jpg");

            Drink americano = new Drink();
            americano.setName("Американо");
            americano.setPrice(40.0);
            americano.setPortions(20);
            americano.setImageName("americano.jpg");

            Drink cappuccino = new Drink();
            cappuccino.setName("Капучино");
            cappuccino.setPrice(55.0);
            cappuccino.setPortions(15);
            cappuccino.setImageName("cappuccino.jpg");

            Drink latte = new Drink();
            latte.setName("Лате");
            latte.setPrice(60.0);
            latte.setPortions(15);
            latte.setImageName("latte.jpg");

            drinkRepository.save(espresso);
            drinkRepository.save(americano);
            drinkRepository.save(cappuccino);
            drinkRepository.save(latte);
        }

        if (ingredientRepository.count() == 0) {
            Ingredient sugar = new Ingredient();
            sugar.setName("Цукор");
            sugar.setAmount(100);

            Ingredient milk = new Ingredient();
            milk.setName("Молоко");
            milk.setAmount(50);

            Ingredient cream = new Ingredient();
            cream.setName("Вершки");
            cream.setAmount(40);

            Ingredient syrup = new Ingredient();
            syrup.setName("Сироп");
            syrup.setAmount(30);

            ingredientRepository.save(sugar);
            ingredientRepository.save(milk);
            ingredientRepository.save(cream);
            ingredientRepository.save(syrup);
        }
    }
}