package com.coffemachine.coffemachine.controller.admin;

import com.coffemachine.coffemachine.service.admin.AdminService;
import jakarta.servlet.http.HttpSession;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

@Controller
public class AdminController {

    private final AdminService adminService;

    public AdminController(AdminService adminService) {
        this.adminService = adminService;
    }

    @GetMapping("/admin")
    public String adminPage(Model model, HttpSession session) {

        String role = (String) session.getAttribute("role");

        if (!"ADMIN".equals(role)) {
            return "redirect:/login";
        }

        model.addAttribute("users", adminService.getAllUsers());
        model.addAttribute("drinks", adminService.getAllDrinks());
        model.addAttribute("orders", adminService.getAllOrders());
        model.addAttribute("ingredients", adminService.getAllIngredients());

        return "admin/index";
    }

    @PostMapping("/admin/drinks/add")
    public String addDrink(@RequestParam String name,
                           @RequestParam Double price,
                           @RequestParam Integer portions,
                           @RequestParam String imageName,
                           HttpSession session) {

        String role = (String) session.getAttribute("role");

        if (!"ADMIN".equals(role)) {
            return "redirect:/login";
        }

        adminService.addDrink(name, price, portions, imageName);

        return "redirect:/admin";
    }

    @GetMapping("/admin/drinks/delete/{id}")
    public String deleteDrink(@PathVariable Long id,
                              HttpSession session,
                              org.springframework.web.servlet.mvc.support.RedirectAttributes redirectAttributes) {

        String role = (String) session.getAttribute("role");

        if (!"ADMIN".equals(role)) {
            return "redirect:/login";
        }

        boolean deleted = adminService.deleteDrink(id);

        if (deleted) {
            redirectAttributes.addFlashAttribute("message", "Напій успішно видалено");
        } else {
            redirectAttributes.addFlashAttribute("error", "Напій не можна видалити, бо він уже використовується в замовленнях");
        }

        return "redirect:/admin";
    }

    @GetMapping("/admin/drinks/edit/{id}")
    public String editDrinkPage(@PathVariable Long id,
                                Model model,
                                HttpSession session) {

        String role = (String) session.getAttribute("role");

        if (!"ADMIN".equals(role)) {
            return "redirect:/login";
        }

        model.addAttribute("drink", adminService.getDrinkById(id));

        return "admin/edit-drink";
    }

    @PostMapping("/admin/drinks/edit/{id}")
    public String updateDrink(@PathVariable Long id,
                              @RequestParam String name,
                              @RequestParam Double price,
                              @RequestParam Integer portions,
                              @RequestParam String imageName,
                              HttpSession session) {

        String role = (String) session.getAttribute("role");

        if (!"ADMIN".equals(role)) {
            return "redirect:/login";
        }

        adminService.updateDrink(id, name, price, portions, imageName);

        return "redirect:/admin";
    }

    @PostMapping("/admin/drinks/refill/{id}")
    public String refillDrink(@PathVariable Long id,
                              @RequestParam Integer portions,
                              HttpSession session) {

        String role = (String) session.getAttribute("role");

        if (!"ADMIN".equals(role)) {
            return "redirect:/login";
        }

        adminService.refillDrink(id, portions);

        return "redirect:/admin";
    }

    @PostMapping("/admin/ingredients/add")
    public String addIngredient(@RequestParam String name,
                                @RequestParam Integer amount,
                                HttpSession session) {

        String role = (String) session.getAttribute("role");

        if (!"ADMIN".equals(role)) {
            return "redirect:/login";
        }

        adminService.addIngredient(name, amount);

        return "redirect:/admin";
    }

    @PostMapping("/admin/ingredients/refill/{id}")
    public String refillIngredient(@PathVariable Long id,
                                   @RequestParam Integer amount,
                                   HttpSession session) {

        String role = (String) session.getAttribute("role");

        if (!"ADMIN".equals(role)) {
            return "redirect:/login";
        }

        adminService.refillIngredient(id, amount);

        return "redirect:/admin";
    }

    @GetMapping("/admin/ingredients/delete/{id}")
    public String deleteIngredient(@PathVariable Long id,
                                   HttpSession session) {

        String role = (String) session.getAttribute("role");

        if (!"ADMIN".equals(role)) {
            return "redirect:/login";
        }

        adminService.deleteIngredient(id);

        return "redirect:/admin";
    }
}