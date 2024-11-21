package com.example.myapplication;

public class User {
    private String email;
    private String role;

    public User() {
        // Default constructor required for Firestore
    }

    public User(String email, String role) {
        this.email = email;
        this.role = role;
    }

    public String getEmail() {
        return email;
    }

    public String getRole() {
        return role;
    }
}
