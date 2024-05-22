package com.example.cupcakesshop;
import java.io.Serializable;

public class Cupcake implements Serializable {
    private String name;
    private String category;
    private float price;

    public Cupcake() {
        // Default constructor required for calls to DataSnapshot.getValue(Cupcake.class)
    }

    public Cupcake(String name, String category, float price) {
        this.name = name;
        this.category = category;
        this.price = price;
    }

    // Getters and setters
    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getCategory() {
        return category;
    }

    public void setCategory(String category) {
        this.category = category;
    }

    public float getPrice() {
        return price;
    }

    public void setPrice(float price) {
        this.price = price;
    }

}
