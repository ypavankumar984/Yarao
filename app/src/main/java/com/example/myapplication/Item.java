package com.example.myapplication;

public class Item {
    private String itemName;
    private double itemCost; // Changed to double

    // Constructor
    public Item(String itemName, double itemCost) {
        this.itemName = itemName;
        this.itemCost = itemCost;
    }

    // Getters
    public String getItemName() {
        return itemName;
    }

    public double getItemCost() {
        return itemCost; // Returns a double
    }

    // Setters
    public void setItemName(String itemName) {
        this.itemName = itemName;
    }

    public void setItemCost(double itemCost) {
        this.itemCost = itemCost; // Sets a double value
    }
}
