package com.shunsu22.pantryai.repository;

import com.shunsu22.pantryai.entity.Ingredient;
import com.shunsu22.pantryai.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface IngredientRepository extends JpaRepository<Ingredient, Long> {

    List<Ingredient> findByUserOrderByPurchasedAtAsc(User user);

}
