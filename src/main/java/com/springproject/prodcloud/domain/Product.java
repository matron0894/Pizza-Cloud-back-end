package com.springproject.prodcloud.domain;

import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.*;
import org.springframework.data.rest.core.annotation.RestResource;

import javax.persistence.*;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;


@Entity
@Getter
@Setter
@RestResource(rel="products", path="products")
@RequiredArgsConstructor
public class Product {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    @Column(name = "ID", nullable = false)
    @JsonIgnore
    private Long Id;

    @NotBlank
    @Size(min = 5, message = "Name must be at least 5 characters long")
    @Column(nullable = false)
    private String name;

    @Temporal(TemporalType.DATE)
    @Column(/*name = "created_date",*/ nullable = false, updatable = false)
    private Date createdAt;

    @PrePersist
    void createdAt() {
        this.createdAt = new Date();
    }


    @ManyToMany//(targetEntity = Ingredient.class, cascade = CascadeType.MERGE)
    @NotNull
    @Size(min = 1, message = "You must choose at least 1 ingredient")
    @JoinTable(
            name = "selected_ingredients",
            joinColumns = @JoinColumn(name = "shaverma_id"),
            inverseJoinColumns = @JoinColumn(name = "ingredient_id"))
    private List<Ingredient> ingredients = new ArrayList<>();

    public void addIngredient(Ingredient ingredient) {
        this.ingredients.add(ingredient);
    }

//    @ManyToMany(mappedBy = "products")
//    private List<Order> orders = new ArrayList<>();
}
