package com.springproject.prodcloud.controller;

import com.springproject.prodcloud.domain.Ingredient;
import com.springproject.prodcloud.domain.Ingredient.Type;
import com.springproject.prodcloud.domain.Order;
import com.springproject.prodcloud.domain.Product;
import com.springproject.prodcloud.domain.User;
import com.springproject.prodcloud.oauth2.CustomOAuth2User;
import com.springproject.prodcloud.repos.IngredientRepository;
import com.springproject.prodcloud.repos.ProductRepository;
import com.springproject.prodcloud.repos.UserRepository;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.security.oauth2.client.authentication.OAuth2AuthenticationToken;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.Errors;
import org.springframework.web.bind.annotation.*;

import javax.transaction.Transactional;
import javax.validation.Valid;
import java.security.Principal;
import java.util.List;
import java.util.stream.Collectors;

@Slf4j
@Controller
@Transactional
@RequestMapping("/design")
@SessionAttributes(value = "order")
public class DesignShavermaController {

    private final UserRepository userRepository;
    private final IngredientRepository ingredientRepo;
    private final ProductRepository productRepository;

    @Autowired
    public DesignShavermaController(UserRepository userRepository, IngredientRepository ingredientRepo, ProductRepository productRepository) {
        this.userRepository = userRepository;
        this.ingredientRepo = ingredientRepo;
        this.productRepository = productRepository;
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

//    @ModelAttribute(name = "user")
//    public User user(@AuthenticationPrincipal User user) {
//        String username = principal.getName();
//        return userRepository.findByUsername(username);
//    }


    @ModelAttribute
    public void addIngredientsToModel(Model model) {
        List<Ingredient> ingredients = ingredientRepo.findAll();

        Type[] types = Ingredient.Type.values();
        for (Type type : types) {
            model.addAttribute(type.toString().toLowerCase(),
                    filterByType(ingredients, type));
        }
    }

    @ModelAttribute(name = "user")
    public User user(Principal principal) {
        String username;
        if (principal instanceof OAuth2AuthenticationToken) {
            CustomOAuth2User ud = (CustomOAuth2User) ((Authentication) principal).getPrincipal();
            username = ud.getEmail();
        } else {
            username = principal.getName();
        }
        return userRepository.findByUsername(username);
    }


    @GetMapping
    public String showDesignForm(Model model) {
        if (!model.containsAttribute("order"))
            model.addAttribute("order", new Order());
        model.addAttribute("product", new Product());
        return "design";
    }


    @PostMapping
    public String processDesign(@Valid @ModelAttribute("product") Product product,
                                @ModelAttribute("user") User user,
                                Errors errors,
                                @ModelAttribute(name = "order") Order order) {
//        // Save the shvm design…
        log.info("   --- Processing product");
        if (errors.hasErrors()) {
            return "design";
        }
        log.info("   --- Saving product");
        Product newProd = productRepository.save(product);
        order.setUser(user);
        order.addProduct(newProd);
        return "redirect:/orders/current";
    }


    private List<Ingredient> filterByType(List<Ingredient> ingredients, Type type) {
        return ingredients.stream()
                .filter(x -> x.getType().equals(type))
                .collect(Collectors.toList());

    }

}
