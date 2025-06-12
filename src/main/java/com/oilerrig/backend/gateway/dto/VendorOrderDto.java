package com.oilerrig.backend.gateway.dto;

import java.util.UUID;

public class VendorOrderDto {
    private UUID id;
    private String productId; // The product that was ordered
    private Integer quantity;
    private String status; // E.g., "PENDING", "COMPLETED"

    // Constructors
    public VendorOrderDto() {}

    public VendorOrderDto(UUID orderId, String productId, Integer quantity, String status) {
        this.id = orderId;
        this.productId = productId;
        this.quantity = quantity;
        this.status = status;
    }

    // Getters and Setters
    public UUID getId() {
        return id;
    }

    public void setId(UUID orderId) {
        this.id = orderId;
    }

    public String getProductId() {
        return productId;
    }

    public void setProductId(String productId) {
        this.productId = productId;
    }

    public Integer getQuantity() {
        return quantity;
    }

    public void setQuantity(Integer quantity) {
        this.quantity = quantity;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    @Override
    public String toString() {
        return "VendorOrderDto{" +
                "orderId='" + id + '\'' +
                ", productId='" + productId + '\'' +
                ", quantity=" + quantity +
                ", status='" + status + '\'' +
                '}';
    }
}