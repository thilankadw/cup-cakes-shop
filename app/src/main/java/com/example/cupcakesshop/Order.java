package com.example.cupcakesshop;

public class Order {
    private String orderId;
    private String name;
    private String category;
    private int quantity;
    private double price;
    private String status;
    private String userId;

    public Order() {
        // Default constructor required for calls to DataSnapshot.getValue(Order.class)
    }

    public Order(String orderId, String name, String category, int quantity, double price,String status, String userId) {
        this.orderId = orderId;
        this.name = name;
        this.category = category;
        this.quantity = quantity;
        this.price = price;
        this.status = status;
        this.userId = userId;
    }

    public Order(String orderId, String name, String category, int quantity, float price, String status, String userId) {
        this.orderId = orderId;
        this.name = name;
        this.category = category;
        this.quantity = quantity;
        this.price = price;
        this.status = status;
        this.userId = userId;
    }

    public String getOrderId() {
        return orderId;
    }

    public void setOrderId(String orderId) {
        this.orderId = orderId;
    }

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

    public int getQuantity() {
        return quantity;
    }

    public void setQuantity(int quantity) {
        this.quantity = quantity;
    }

    public double getPrice() {
        return price;
    }

    public void setPrice(double price) {
        this.price = price;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public String getUserId() {
        return userId;
    }

    public void setUserId(String userId) {
        this.userId = userId;
    }
}
