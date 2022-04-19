package com.springproject.shavermacloud.controller;

import com.springproject.shavermacloud.domain.Ingredient;
import com.springproject.shavermacloud.domain.Ingredient.Type;
import com.springproject.shavermacloud.domain.Order;
import com.springproject.shavermacloud.domain.Shaverma;
import com.springproject.shavermacloud.repos.IngredientRepository;
import com.springproject.shavermacloud.repos.ShavermaRepository;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.Errors;
import org.springframework.web.bind.annotation.*;

import javax.transaction.Transactional;
import javax.validation.Valid;
import java.util.List;
import java.util.stream.Collectors;

@Slf4j
@Controller
@Transactional
@RequestMapping("/design")
@SessionAttributes("order")
public class DesignShavermaController {

    private final IngredientRepository ingredientRepo;
    private final ShavermaRepository shavermaRepository;

    @Autowired
    public DesignShavermaController(IngredientRepository ingredientRepo, ShavermaRepository shavermaRepository) {
        this.ingredientRepo = ingredientRepo;
        this.shavermaRepository = shavermaRepository;
    }

//    @ModelAttribute(name = "order")
//    public Order order() {
//        return new Order();
//    }
//
//    @ModelAttribute(name = "shaverma")
//    public Shaverma design() {
//        return new Shaverma();
//    }


    @ModelAttribute
    public void addIngredientsToModel(Model model) {
//        List<Ingredient> ingredients = Arrays.asList(
//                new Ingredient("FLTO", "Flour Tortilla", Type.WRAP),
//                new Ingredient("COTO", "Corn Tortilla", Type.WRAP),
//                new Ingredient("GRBF", "Ground Beef", Type.PROTEIN),
//                new Ingredient("CARN", "Carnitas", Type.PROTEIN),
//                new Ingredient("TMTO", "Diced Tomatoes", Type.VEGGIES),
//                new Ingredient("LETC", "Lettuce", Type.VEGGIES),
//                new Ingredient("CHED", "Cheddar", Type.CHEESE),
//                new Ingredient("JACK", "Monterrey Jack", Type.CHEESE),
//                new Ingredient("SLSA", "Salsa", Type.SAUCE),
//                new Ingredient("SRCR", "Sour Cream", Type.SAUCE)
//        );
        List<Ingredient> ingredients = ingredientRepo.findAll();

        Type[] types = Ingredient.Type.values();
        for (Type type : types) {
            model.addAttribute(type.toString().toLowerCase(),
                    filterByType(ingredients, type));
        }
    }

    @GetMapping
    public String showDesignForm(Model model) {
//        List<Ingredient> ingredients = new ArrayList<>(ingredientRepo.findAll());
//        Type[] types = Ingredient.Type.values();
//        log.info("   --- Show Design shvm");
//        for (Type type : types) {
//            model.addAttribute(type.toString().toLowerCase(), filterByType(ingredients, type));
//        }
        model.addAttribute("order", new Order());
        model.addAttribute("shaverma", new Shaverma());
        return "design";
    }


    @PostMapping
    public String processDesign(@Valid @ModelAttribute("shaverma") Shaverma shaverma,
                                Errors errors,
                                @ModelAttribute(name = "order") Order order) {
//        // Save the shvm design…
        log.info("   --- Processing taco");
        if (errors.hasErrors()) {
            return "design";
        }
        log.info("   --- Saving shaverma");
        Shaverma newShav = shavermaRepository.save(shaverma);
        order.addProduct(newShav);
        return "redirect:/orders/current";
    }


    private List<Ingredient> filterByType(List<Ingredient> ingredients, Type type) {
        return ingredients.stream()
                .filter(x -> x.getType().equals(type))
                .collect(Collectors.toList());

    }

}
