package com.shunsu22.pantryai.controller;

import com.shunsu22.pantryai.entity.Ingredient;
import com.shunsu22.pantryai.entity.User;
import com.shunsu22.pantryai.repository.UserRepository;
import com.shunsu22.pantryai.service.IngredientService;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDate;
import java.time.temporal.ChronoUnit;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Controller
@RequestMapping("/ingredients")
@RequiredArgsConstructor
public class IngredientController {

    private final IngredientService ingredientService;
    private final UserRepository userRepository;

    private User getLoginUser(UserDetails userDetails) {
        return userRepository.findByUsername(userDetails.getUsername())
                .orElseThrow(() -> new IllegalArgumentException("ユーザーが見つかりません"));
    }

    @GetMapping
    public String list(@AuthenticationPrincipal UserDetails userDetails, Model model) {
        User user = getLoginUser(userDetails);
        List<Ingredient> ingredients = ingredientService.findByUser(user);
        LocalDate today = LocalDate.now();

        // 経過日数をMapで計算して渡す
        Map<Long, Long> daysSincePurchase = ingredients.stream()
                .collect(Collectors.toMap(
                        Ingredient::getId,
                        i -> ChronoUnit.DAYS.between(i.getPurchasedAt(), today)
                ));

        model.addAttribute("ingredients", ingredients);
        model.addAttribute("daysSincePurchase", daysSincePurchase);
        model.addAttribute("today", today);
        return "ingredients/list";
    }

    @GetMapping("/new")
    public String newForm(Model model) {
        model.addAttribute("ingredient", new Ingredient());
        return "ingredients/form";
    }

    @PostMapping("/new")
    public String create(@AuthenticationPrincipal UserDetails userDetails,
                         @ModelAttribute Ingredient ingredient) {
        User user = getLoginUser(userDetails);
        ingredient.setUser(user);
        ingredientService.save(ingredient);
        return "redirect:/ingredients";
    }

    @GetMapping("/{id}/edit")
    public String editForm(@PathVariable Long id, Model model) {
        model.addAttribute("ingredient", ingredientService.findById(id));
        return "ingredients/form";
    }

    @PostMapping("/{id}/edit")
    public String update(@PathVariable Long id,
                         @ModelAttribute Ingredient ingredient) {
        Ingredient existing = ingredientService.findById(id);
        existing.setName(ingredient.getName());
        existing.setQuantity(ingredient.getQuantity());
        existing.setUnit(ingredient.getUnit());
        existing.setPurchasedAt(ingredient.getPurchasedAt());
        ingredientService.save(existing);
        return "redirect:/ingredients";
    }

    @PostMapping("/{id}/delete")
    public String delete(@PathVariable Long id) {
        ingredientService.delete(id);
        return "redirect:/ingredients";
    }
}