package com.springproject.shavermacloud.email.dao;

import lombok.Data;

import java.util.List;

@Data
public class EmailProduct {
    private final String name;
    private List<String> ingredients;
}
