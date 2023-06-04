package com.example.luxuryservice.Responses;

import com.example.luxuryservice.CartElement;

import java.util.ArrayList;
import java.util.List;

public class OrderUser {

    private final String name;
    private final String gender;
    private final List<CartElement> items;

    public OrderUser(String name, String gender, List<CartElement> items) {
        this.name = name;
        this.items = new ArrayList<>(items);
        this.gender = gender;
    }

    public String getName() {
        return name;
    }

    public String getGender() {
        return gender;
    }

    public List<CartElement> getItems() {
        return items;
    }
}
