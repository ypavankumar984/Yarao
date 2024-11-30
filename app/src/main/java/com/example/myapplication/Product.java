package com.example.myapplication;
public class Product {
    private String name;
    private String cost;

    public Product(String name, String cost) {
        this.name = name;
        this.cost = cost;
    }

    public String getName() {
        return name;
    }

    public String getCost() {
        return cost;
    }
}
