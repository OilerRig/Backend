package com.oilerrig.backend.domain;

public class OrderItem {
    private Product product;
    private int quantity;

    // business logic and helpers
    public boolean isValid() {
        return this.product != null
                && this.quantity > 0
                && this.product.isValid();
    }

    // getters and setters
    public Product getProduct() {
        return product;
    }

    public void setProduct(Product product) {
        this.product = product;
    }

    public int getQuantity() {
        return quantity;
    }

    public void setQuantity(int quantity) {
        this.quantity = quantity;
    }
}
