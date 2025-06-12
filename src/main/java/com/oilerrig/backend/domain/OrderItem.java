package com.oilerrig.backend.domain;

import com.oilerrig.backend.data.entity.OrderItemEntity;

import java.util.UUID;

public class OrderItem {
    private int id;
    private UUID orderId;
    private Product product;
    private int quantity;
    private UUID vendorOrderId;
    private ItemStatus status;

    public enum ItemStatus {
        PENDING,
        COMPLETED,
        FAILED,
        CANCELLED
    }


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

    public ItemStatus getStatus() {
        return status;
    }

    public void setStatus(ItemStatus status) {
        this.status = status;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public UUID getOrderId() {
        return orderId;
    }

    public void setOrderId(UUID orderId) {
        this.orderId = orderId;
    }

    public UUID getVendorOrderId() {
        return vendorOrderId;
    }

    public void setVendorOrderId(UUID vendorOrderId) {
        this.vendorOrderId = vendorOrderId;
    }
}
