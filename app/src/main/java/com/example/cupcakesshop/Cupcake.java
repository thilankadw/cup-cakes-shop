package com.example.cupcakesshop;

import java.io.Serializable;

public class Cupcake implements Serializable {

    public String name;
    public String category;
    public Float price;
    public String imageUrl;

    public Cupcake() { }

    public Cupcake(String name, String category, Float price, String imageUrl){
        this.name = name;
        this.category = category;
        this.price = price;
        this.imageUrl = imageUrl;
    }

    public String getName() {
        return name;
    }

    public String getCategory() {
        return category;
    }

    public String getPrice() {
        return String.format("%.2f", price);
    }

    public String getImageId() {
        return imageUrl;
    }

    public void setImageUrl(String imageUrl) {
        this.imageUrl = imageUrl;
    }

    public Object getImageUrl() {
        return this.imageUrl;
    }
}
