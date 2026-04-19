package com.shunsu22.pantryai.repository;

import com.shunsu22.pantryai.entity.Recipe;
import com.shunsu22.pantryai.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface RecipeRepository extends JpaRepository<Recipe, Long> {

    List<Recipe> findByUserAndIsDeletedFalse(User user);

    List<Recipe> findByUserAndIsFavoriteTrueAndIsDeletedFalse(User user);

}
