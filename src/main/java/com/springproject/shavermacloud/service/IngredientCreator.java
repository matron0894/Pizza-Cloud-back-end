package com.springproject.shavermacloud.service;

import com.springproject.shavermacloud.domain.Ingredient;
import com.springproject.shavermacloud.repos.IngredientRepository;

import java.util.Arrays;
import java.util.List;

public class IngredientCreator {

    private final IngredientRepository ingredientRepos;

    public IngredientCreator(IngredientRepository ingredientRepository) {
        this.ingredientRepos = ingredientRepository;
        create();
    }

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
