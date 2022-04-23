package com.springproject.shavermacloud.service;

import com.springproject.shavermacloud.domain.Ingredient;
import com.springproject.shavermacloud.repos.IngredientRepository;
import org.springframework.core.convert.converter.Converter;

//@Component
class IngredientByIdConverter implements Converter<String, Ingredient> {

    private final IngredientRepository ingredientRepo;

   // @Autowired
    public IngredientByIdConverter(IngredientRepository ingredientRepo) {
        this.ingredientRepo = ingredientRepo;
    }

   @Override
    public Ingredient convert(String id) {
        return ingredientRepo.findById(id).orElse(null);
    }
}
