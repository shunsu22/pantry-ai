package com.shunsu22.pantryai.controller;

import com.shunsu22.pantryai.entity.Ingredient;
import com.shunsu22.pantryai.entity.Recipe;
import com.shunsu22.pantryai.entity.User;
import com.shunsu22.pantryai.repository.UserRepository;
import com.shunsu22.pantryai.service.IngredientService;
import com.shunsu22.pantryai.service.RecipeService;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.stream.Collectors;

@Controller
@RequestMapping("/recipes")
@RequiredArgsConstructor
public class RecipeController {

    private final RecipeService recipeService;
    private final IngredientService ingredientService;
    private final UserRepository userRepository;

    private User getLoginUser(UserDetails userDetails) {
        return userRepository.findByUsername(userDetails.getUsername())
                .orElseThrow(() -> new IllegalArgumentException("ユーザーが見つかりません"));
    }

    @GetMapping
    public String list(@AuthenticationPrincipal UserDetails userDetails, Model model) {
        User user = getLoginUser(userDetails);
        model.addAttribute("recipes", recipeService.findByUser(user));
        return "recipes/list";
    }

    @PostMapping("/suggest")
    public String suggest(@AuthenticationPrincipal UserDetails userDetails, Model model) {
        User user = getLoginUser(userDetails);

        List<String> ingredientNames = ingredientService.findByUser(user)
                .stream()
                .map(Ingredient::getName)
                .collect(Collectors.toList());

        if (ingredientNames.isEmpty()) {
            model.addAttribute("error", "食材が登録されていません");
            model.addAttribute("recipes", recipeService.findByUser(user));
            return "recipes/list";
        }

        String suggestion = recipeService.suggestRecipe(ingredientNames);
        model.addAttribute("suggestion", suggestion);
        model.addAttribute("recipes", recipeService.findByUser(user));
        return "recipes/list";
    }

    @PostMapping("/save")
    public String save(@AuthenticationPrincipal UserDetails userDetails,
                       @RequestParam String name,
                       @RequestParam String description) {
        User user = getLoginUser(userDetails);
        Recipe recipe = new Recipe();
        recipe.setUser(user);
        recipe.setName(name);
        recipe.setDescription(description);
        recipeService.save(recipe);
        return "redirect:/recipes";
    }

    @PostMapping("/{id}/favorite")
    public String toggleFavorite(@PathVariable Long id) {
        recipeService.toggleFavorite(id);
        return "redirect:/recipes";
    }

    @PostMapping("/{id}/delete")
    public String delete(@PathVariable Long id) {
        Recipe recipe = recipeService.findById(id);
        recipe.setIsDeleted(true);
        recipeService.save(recipe);
        return "redirect:/recipes";
    }
}