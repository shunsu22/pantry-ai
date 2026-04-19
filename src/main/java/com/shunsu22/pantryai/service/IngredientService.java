package com.shunsu22.pantryai.service;

import com.shunsu22.pantryai.entity.Ingredient;
import com.shunsu22.pantryai.entity.User;

import java.util.List;

public interface IngredientService {

    List<Ingredient> findByUser(User user);

    Ingredient save(Ingredient ingredient);

    Ingredient findById(Long id);

    void delete(Long id);
}