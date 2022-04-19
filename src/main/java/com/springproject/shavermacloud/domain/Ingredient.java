package com.springproject.shavermacloud.domain;


import lombok.*;

import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;


@Entity
@Getter
@Setter
@RequiredArgsConstructor
@NoArgsConstructor(access = AccessLevel.PRIVATE, force = true)
@Table(name = "INGREDIENTS")
public class Ingredient {

    @Id
    private final String id;

    private final String name;

    private final Type type;


    public enum Type {
        WRAP, PROTEIN, VEGGIES, CHEESE, SAUCE
    }

//    @ManyToMany(mappedBy = "ingredients")
//    private List<Shaverma> shaverms = new ArrayList<>();
}
