package com.oilerrig.backend.gateway.dto;

public class VendorCancelOrderDto {
    private String orderId; // The vendor's order ID to cancel

    // Constructors
    public VendorCancelOrderDto() {}

    public VendorCancelOrderDto(String orderId) {
        this.orderId = orderId;
    }

    // Getter and Setter
    public String getOrderId() {
        return orderId;
    }

    public void setOrderId(String orderId) {
        this.orderId = orderId;
    }

    @Override
    public String toString() {
        return "VendorCancelOrderDto{" +
                "orderId='" + orderId + '\'' +
                '}';
    }
}