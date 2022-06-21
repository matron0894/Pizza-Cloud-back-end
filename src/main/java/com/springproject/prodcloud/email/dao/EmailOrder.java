package com.springproject.prodcloud.email.dao;


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