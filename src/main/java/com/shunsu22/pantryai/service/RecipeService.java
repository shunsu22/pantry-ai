package com.shunsu22.pantryai.service;

import com.shunsu22.pantryai.entity.Recipe;
import com.shunsu22.pantryai.entity.User;

import java.util.List;

public interface RecipeService {
    String suggestRecipe(List<String> ingredientNames);
    void save(Recipe recipe);
    List<Recipe> findByUser(User user);
    Recipe findById(Long id);
    void toggleFavorite(Long id);
}