package com.springproject.shavermacloud.email.dao;


import com.springproject.shavermacloud.domain.Product;
import lombok.Data;

import java.util.ArrayList;
import java.util.List;

@Data
public class EmailOrder {

    private final String email;

    private List<EmailProduct> tacos = new ArrayList<>();

    public void addTaco(EmailProduct product) {
        this.tacos.add(product);
    }

}