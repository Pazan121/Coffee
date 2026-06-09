package com.coffemachine.coffemachine.controller;

import com.coffemachine.coffemachine.entity.User;
import com.coffemachine.coffemachine.repository.OrderRepository;
import com.coffemachine.coffemachine.repository.UserRepository;
import jakarta.servlet.http.HttpSession;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

@Controller
public class ProfileController {

    private final UserRepository userRepository;
    private final OrderRepository orderRepository;

    public ProfileController(UserRepository userRepository,
                             OrderRepository orderRepository) {
        this.userRepository = userRepository;
        this.orderRepository = orderRepository;
    }

    @GetMapping("/profile")
    public String profilePage(Model model, HttpSession session) {

        Long userId = (Long) session.getAttribute("userId");

        if (userId == null) {
            return "redirect:/login";
        }

        User user = userRepository.findById(userId).orElse(null);

        if (user == null) {
            return "redirect:/login";
        }

        model.addAttribute("user", user);
        model.addAttribute("ordersCount", orderRepository.countByUser(user));

        return "profile";
    }

    @PostMapping("/profile/top-up")
    public String topUpBalance(@RequestParam String cardNumber,
                               @RequestParam String expiryDate,
                               @RequestParam String cvv,
                               @RequestParam Double amount,
                               HttpSession session,
                               RedirectAttributes redirectAttributes) {

        Long userId = (Long) session.getAttribute("userId");

        if (userId == null) {
            return "redirect:/login";
        }

        User user = userRepository.findById(userId).orElse(null);

        if (user == null) {
            return "redirect:/login";
        }

        String cleanCardNumber = cardNumber.replaceAll("\\s+", "");

        if (!cleanCardNumber.matches("\\d{16}")) {
            redirectAttributes.addFlashAttribute("error", "Номер картки має містити 16 цифр");
            return "redirect:/profile";
        }

        if (expiryDate == null || expiryDate.isBlank()) {
            redirectAttributes.addFlashAttribute("error", "Потрібно вказати термін дії картки");
            return "redirect:/profile";
        }

        if (!cvv.matches("\\d{3}")) {
            redirectAttributes.addFlashAttribute("error", "CVV має містити 3 цифри");
            return "redirect:/profile";
        }

        if (amount == null || amount <= 0) {
            redirectAttributes.addFlashAttribute("error", "Сума поповнення має бути більшою за 0");
            return "redirect:/profile";
        }

        user.setBalance(user.getBalance() + amount);
        userRepository.save(user);

        redirectAttributes.addFlashAttribute(
                "message",
                "Рахунок успішно поповнено на " + amount + " грн"
        );

        return "redirect:/profile";
    }
}