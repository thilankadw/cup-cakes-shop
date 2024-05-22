package com.example.cupcakesshop;

import java.util.Date;

public class Order {
    private String orderId;
    private String name;
    private String category;
    private int quantity;
    private float price;
    private String status = "Pending";
    private Date date;
    private String userId;

    public Order() {
        // Default constructor required for calls to DataSnapshot.getValue(Order.class)
    }

    public Order(String orderId, String name, String category, int quantity, float price, String status, String userId) {
        this.orderId = orderId;
        this.name = name;
        this.category = category;
        this.quantity = quantity;
        this.price = price;
        this.status = status;
        this.date = new Date();
        this.userId = userId;
    }

    public String getOrderId() {
        return orderId;
    }

    public String getName() {
        return name;
    }

    public String getCategory() {
        return category;
    }

    public int getQuantity() {
        return quantity;
    }

    public float getPrice() {
        return price;
    }

    public String getStatus() {
        return status;
    }

    public Date getDate() {
        return date;
    }

    public String getUserId() {
        return userId;
    }

    public void setStatus(String newStatus) {
        this.status = newStatus;
    }
}
