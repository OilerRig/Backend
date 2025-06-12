package com.oilerrig.backend.gateway.dto;

import java.util.UUID;

public class VendorCancelOrderDto {
    private UUID orderId; // The vendor's order ID to cancel

    // Constructors
    public VendorCancelOrderDto() {}

    public VendorCancelOrderDto(UUID orderId) {
        this.orderId = orderId;
    }

    // Getter and Setter
    public UUID getOrderId() {
        return orderId;
    }

    public void setOrderId(UUID orderId) {
        this.orderId = orderId;
    }

    @Override
    public String toString() {
        return "VendorCancelOrderDto{" +
                "orderId='" + orderId + '\'' +
                '}';
    }
}