package com.example.myapplication;

public class Shop {
    private String shopName;
    private String ownerName;
    private String contactNo;
    private String address;

    public Shop(String shopName, String ownerName, String contactNo, String address) {
        this.shopName = shopName;
        this.ownerName = ownerName;
        this.contactNo = contactNo;
        this.address = address;
    }

    // Getters and setters
    public String getShopName() {
        return shopName;
    }

    public String getOwnerName() {
        return ownerName;
    }

    public String getContactNo() {
        return contactNo;
    }

    public String getAddress() {
        return address;
    }
}
