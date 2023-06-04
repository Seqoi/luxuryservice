package com.example.luxuryservice;

import java.io.Serializable;

public class CartElement implements Serializable {

    private int id;
    private int amount;
    private String name;
    private String description;
    private String price;
    private String category;
    private String time_result;
    private String preparation;
    private String bio;

    public CartElement(int id, String name, String description, String price, String category, String time_result, String preparation, String bio) {
        this.id = id;
        this.amount = 1;
        this.name = name;
        this.description = description;
        this.price = price;
        this.category = category;
        this.time_result = time_result;
        this.preparation = preparation;
        this.bio = bio;
    }

    public int getId() {
        return id;
    }

    public int getAmount() {
        return amount;
    }

    public void setAmount(int amount) {
        this.amount = amount;
    }

    public String getName() {
        return name;
    }

    public String getPrice() {
        return price;
    }


    public String getDescription() {
        return description;
    }

    public String getCategory() {
        return category;
    }

    public String getTime_result() {
        return time_result;
    }

    public String getPreparation() {
        return preparation;
    }

    public String getBio() {
        return bio;
    }
}
