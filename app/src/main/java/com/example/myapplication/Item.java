package com.example.myapplication;

public class Item {
    private String itemName;
    private String itemCost;

    public Item(String itemName, String itemCost) {
        this.itemName = itemName;
        this.itemCost = itemCost;
    }

    // Getters and setters
    public String getItemName() {
        return itemName;
    }

    public String getItemCost() {
        return itemCost;
    }
}
