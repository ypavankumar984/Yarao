package com.example.myapplication;

public class Product {
    private String name;
    private double cost;
    private String shopName; // Add a shopName field

    // Constructor to initialize all fields
    public Product(String name, double cost, String shopName) {
        this.name = name;
        this.cost = cost;
        this.shopName = shopName;
    }

    public String getName() {
        return name;
    }

    public double getCost() {
        return cost;
    }

    public String getShopName() {
        return shopName;
    }

    // Method to format the cost with Rs.
    public String getFormattedCost() {
        return "Rs." + cost;
    }
}
