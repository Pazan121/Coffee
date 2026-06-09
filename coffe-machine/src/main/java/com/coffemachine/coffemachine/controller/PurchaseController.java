package com.coffemachine.coffemachine.controller;

import com.coffemachine.coffemachine.service.PurchaseService;
import jakarta.servlet.http.HttpSession;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import java.util.List;

@Controller
public class PurchaseController {

    private final PurchaseService purchaseService;

    public PurchaseController(PurchaseService purchaseService) {
        this.purchaseService = purchaseService;
    }

    @GetMapping("/buy/{drinkId}")
    public String buyDrink(@PathVariable Long drinkId,
                           @RequestParam Integer quantity,
                           @RequestParam(required = false) List<Long> ingredients,
                           @RequestParam String pickupLocation,
                           HttpSession session,
                           RedirectAttributes redirectAttributes) {

        Long userId = (Long) session.getAttribute("userId");

        if (userId == null) {
            return "redirect:/login";
        }

        String message = purchaseService.buyDrink(userId, drinkId, quantity, ingredients, pickupLocation);

        redirectAttributes.addFlashAttribute("message", message);

        return "redirect:/drinks";
    }
}