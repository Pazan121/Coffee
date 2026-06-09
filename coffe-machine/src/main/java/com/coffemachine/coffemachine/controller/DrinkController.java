package com.coffemachine.coffemachine.controller;

import com.coffemachine.coffemachine.entity.User;
import com.coffemachine.coffemachine.repository.DrinkRepository;
import com.coffemachine.coffemachine.repository.IngredientRepository;
import com.coffemachine.coffemachine.repository.UserRepository;
import jakarta.servlet.http.HttpSession;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
public class DrinkController {

    private final DrinkRepository drinkRepository;
    private final UserRepository userRepository;
    private final IngredientRepository ingredientRepository;

    public DrinkController(DrinkRepository drinkRepository,
                           UserRepository userRepository,
                           IngredientRepository ingredientRepository) {
        this.drinkRepository = drinkRepository;
        this.userRepository = userRepository;
        this.ingredientRepository = ingredientRepository;
    }

    @GetMapping("/drinks")
    public String drinksPage(Model model, HttpSession session) {

        Long userId = (Long) session.getAttribute("userId");

        if (userId == null) {
            return "redirect:/login";
        }

        User user = userRepository.findById(userId).orElse(null);

        model.addAttribute("drinks", drinkRepository.findAll());
        model.addAttribute("ingredients", ingredientRepository.findAll());
        model.addAttribute("user", user);

        return "drinks";
    }
}