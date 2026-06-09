package com.coffemachine.coffemachine.controller;

import com.coffemachine.coffemachine.service.CartService;
import com.coffemachine.coffemachine.service.PurchaseService;
import jakarta.servlet.http.HttpSession;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import java.util.List;

@Controller
public class CartController {

    private final CartService cartService;
    private final PurchaseService purchaseService;

    public CartController(CartService cartService,
                          PurchaseService purchaseService) {
        this.cartService = cartService;
        this.purchaseService = purchaseService;
    }

    @GetMapping("/cart")
    public String cartPage(Model model, HttpSession session) {

        Long userId = (Long) session.getAttribute("userId");

        if (userId == null) {
            return "redirect:/login";
        }

        model.addAttribute("cart", cartService.getCart(session));
        model.addAttribute("cartTotal", cartService.getCartTotal(session));

        return "cart";
    }

    @GetMapping("/cart/add/{drinkId}")
    public String addToCart(@PathVariable Long drinkId,
                            @RequestParam Integer quantity,
                            @RequestParam(required = false) List<Long> ingredients,
                            @RequestParam String pickupLocation,
                            HttpSession session,
                            RedirectAttributes redirectAttributes) {

        Long userId = (Long) session.getAttribute("userId");

        if (userId == null) {
            return "redirect:/login";
        }

        String message = cartService.addToCart(
                drinkId,
                quantity,
                ingredients,
                pickupLocation,
                session
        );

        redirectAttributes.addFlashAttribute("message", message);

        return "redirect:/drinks";
    }

    @GetMapping("/cart/remove/{index}")
    public String removeFromCart(@PathVariable Integer index,
                                 HttpSession session) {

        cartService.removeFromCart(index, session);
        return "redirect:/cart";
    }

    @GetMapping("/cart/clear")
    public String clearCart(HttpSession session) {
        cartService.clearCart(session);
        return "redirect:/cart";
    }

    @GetMapping("/cart/checkout")
    public String checkout(HttpSession session,
                           RedirectAttributes redirectAttributes) {

        Long userId = (Long) session.getAttribute("userId");

        if (userId == null) {
            return "redirect:/login";
        }

        String message = purchaseService.checkoutCart(userId, cartService.getCart(session));

        if ("Покупку успішно оформлено".equals(message)) {
            cartService.clearCart(session);
        }

        redirectAttributes.addFlashAttribute("message", message);

        return "redirect:/cart";
    }
}