package com.oilerrig.backend.domain;

public class Product {
    private int id;
    private Vendor vendor;
    private int vendorProductId;
    private String name;
    private double price;
    private int stock;

    // business logic and helpers
    public boolean inStock() {
        return stock > 0;
    }

    public boolean isValid() {
        return this.vendor != null
                && this.name != null
                && this.price > 0
                && this.id >= 0
                && this.vendorProductId >= 0
                && this.vendor.isValid();
    }

    // getters and setters
    public Vendor getVendor() {
        return vendor;
    }

    public void setVendor(Vendor vendor) {
        this.vendor = vendor;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public double getPrice() {
        return price;
    }

    public void setPrice(double price) {
        this.price = price;
    }

    public int getStock() {
        return stock;
    }

    public void setStock(int stock) {
        this.stock = stock;
    }

    public int getVendorProductId() {
        return vendorProductId;
    }

    public void setVendorProductId(int vendorProductId) {
        this.vendorProductId = vendorProductId;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }
}
