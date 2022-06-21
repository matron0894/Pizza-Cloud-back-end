package com.springproject.prodcloud.service;

import com.springproject.prodcloud.domain.Ingredient;
import com.springproject.prodcloud.repos.IngredientRepository;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Profile;

import java.util.Arrays;
import java.util.List;

@Configuration
public class IngredientCreator {

    private final IngredientRepository ingredientRepos;

    public IngredientCreator(IngredientRepository ingredientRepository) {
        this.ingredientRepos = ingredientRepository;
        create();
    }

    @Profile("prod")
    private void create() {
        List<Ingredient> ingredients = Arrays.asList(
                new Ingredient("FLTO", "Flour Tortilla", Ingredient.Type.WRAP),
                new Ingredient("COTO", "Corn Tortilla", Ingredient.Type.WRAP),
                new Ingredient("GRBF", "Ground Beef", Ingredient.Type.PROTEIN),
                new Ingredient("CHKN", "Chicken", Ingredient.Type.PROTEIN),
                new Ingredient("TMTO", "Diced Tomatoes", Ingredient.Type.VEGGIES),
                new Ingredient("LETC", "Lettuce", Ingredient.Type.VEGGIES),
                new Ingredient("CHED", "Cheddar", Ingredient.Type.CHEESE),
                new Ingredient("GAUD", "Gauda", Ingredient.Type.CHEESE),
                new Ingredient("MAYO", "Mayonnaise", Ingredient.Type.SAUCE),
                new Ingredient("SRCR", "Sour Cream", Ingredient.Type.SAUCE)
        );

        ingredientRepos.saveAll(ingredients);
    }
}
