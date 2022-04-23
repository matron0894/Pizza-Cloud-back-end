package com.springproject.shavermacloud.service;

import com.springproject.shavermacloud.domain.Ingredient;
import com.springproject.shavermacloud.domain.Product;
import com.springproject.shavermacloud.domain.Role;
import com.springproject.shavermacloud.domain.User;
import com.springproject.shavermacloud.repos.IngredientRepository;
import com.springproject.shavermacloud.repos.ProductRepository;
import com.springproject.shavermacloud.repos.UserRepository;
import org.springframework.boot.CommandLineRunner;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.crypto.password.PasswordEncoder;

import java.util.Arrays;
import java.util.Collections;

@Configuration
public class DevelopmentConfig {

    @Bean
    public CommandLineRunner dataLoader(IngredientRepository repo,
                                        UserRepository userRepo,
                                        PasswordEncoder encoder,
                                        ProductRepository shavermaRepository) { // user repo for ease of testing with a built-in user
        return args -> {
            Ingredient flourTortilla = new Ingredient("FLTO", "Flour Tortilla", Ingredient.Type.WRAP);
            Ingredient cornTortilla = new Ingredient("COTO", "Corn Tortilla", Ingredient.Type.WRAP);
            Ingredient groundBeef = new Ingredient("GRBF", "Ground Beef", Ingredient.Type.PROTEIN);
            Ingredient carnitas = new Ingredient("CARN", "Carnitas", Ingredient.Type.PROTEIN);
            Ingredient tomatoes = new Ingredient("TMTO", "Diced Tomatoes", Ingredient.Type.VEGGIES);
            Ingredient lettuce = new Ingredient("LETC", "Lettuce", Ingredient.Type.VEGGIES);
            Ingredient cheddar = new Ingredient("CHED", "Cheddar", Ingredient.Type.CHEESE);
            Ingredient jack = new Ingredient("JACK", "Monterrey Jack", Ingredient.Type.CHEESE);
            Ingredient salsa = new Ingredient("SLSA", "Salsa", Ingredient.Type.SAUCE);
            Ingredient sourCream = new Ingredient("SRCR", "Sour Cream", Ingredient.Type.SAUCE);
            repo.save(flourTortilla);
            repo.save(cornTortilla);
            repo.save(groundBeef);
            repo.save(carnitas);
            repo.save(tomatoes);
            repo.save(lettuce);
            repo.save(cheddar);
            repo.save(jack);
            repo.save(salsa);
            repo.save(sourCream);

            User user = new User("a", encoder.encode("a"),
                    "Craig Walls", "123 North Street", "Cross Roads", "TX",
                    "76227", "123-123-1234");
            user.setRoles(Collections.singleton(Role.ROLE_USER));
            userRepo.save(user);

            Product prod1 = new Product();
            prod1.setName("Carnivore");
            prod1.setIngredients(Arrays.asList(flourTortilla, groundBeef, carnitas, sourCream, salsa, cheddar));
            shavermaRepository.save(prod1);

            Product prod2 = new Product();
            prod2.setName("Bovine Bounty");
            prod2.setIngredients(Arrays.asList(cornTortilla, groundBeef, cheddar, jack, sourCream));
            shavermaRepository.save(prod2);

            Product prod3 = new Product();
            prod3.setName("Veg-Out");
            prod3.setIngredients(Arrays.asList(flourTortilla, cornTortilla, tomatoes, lettuce, salsa));
            shavermaRepository.save(prod3);

        };
    }
}
