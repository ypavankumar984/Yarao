package com.example.myapplication;


public class UserRole {
    private String email;
    private String role;

    public UserRole() {
        // Default constructor required for Firestore
    }

    public UserRole(String email, String role) {
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
