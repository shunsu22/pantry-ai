package com.shunsu22.pantryai.service.impl;

import com.shunsu22.pantryai.entity.Ingredient;
import com.shunsu22.pantryai.entity.User;
import com.shunsu22.pantryai.repository.IngredientRepository;
import com.shunsu22.pantryai.service.IngredientService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@RequiredArgsConstructor
public class IngredientServiceImpl implements IngredientService {

    private final IngredientRepository ingredientRepository;

    @Override
    public List<Ingredient> findByUser(User user) {
        return ingredientRepository.findByUserOrderByPurchasedAtAsc(user);
    }

    @Override
    @Transactional
    public Ingredient save(Ingredient ingredient) {
        return ingredientRepository.save(ingredient);
    }

    @Override
    public Ingredient findById(Long id) {
        return ingredientRepository.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("食材が見つかりません: " + id));
    }

    @Override
    @Transactional
    public void delete(Long id) {
        ingredientRepository.deleteById(id);
    }
}