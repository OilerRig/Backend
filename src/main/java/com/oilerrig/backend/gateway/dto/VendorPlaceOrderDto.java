package com.oilerrig.backend.gateway.dto;

public class VendorPlaceOrderDto {
    private Integer productId;
    private Integer quantity;

    // Constructors
    public VendorPlaceOrderDto() {}

    public VendorPlaceOrderDto(Integer productId, Integer quantity) {
        this.productId = productId;
        this.quantity = quantity;
    }

    // Getters and Setters
    public Integer getProductId() {
        return productId;
    }

    public void setProductId(Integer productId) {
        this.productId = productId;
    }

    public Integer getQuantity() {
        return quantity;
    }

    public void setQuantity(Integer quantity) {
        this.quantity = quantity;
    }

    @Override
    public String toString() {
        return "VendorPlaceOrderDto{" +
                "productId='" + productId + '\'' +
                ", quantity=" + quantity +
                '}';
    }
}