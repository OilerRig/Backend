package com.oilerrig.backend.data.saga;

import com.oilerrig.backend.data.entity.OrderItemEntity;

import java.io.Serializable;

public class SagaOrderItem implements Serializable {

    private int productId;
    private int quantity;
    private int vendorId;
    private int vendorProductId;

    public SagaOrderItem() {}

    public SagaOrderItem(int productId, int quantity, int vendorId, int vendorProductId) {
        this.productId = productId;
        this.quantity = quantity;
        this.vendorId = vendorId;
        this.vendorProductId = vendorProductId;
    }

    public SagaOrderItem(OrderItemEntity orderItemEntity) {
        this(
                orderItemEntity.getProduct().getId(),
                orderItemEntity.getQuantity(),
                orderItemEntity.getProduct().getVendor().getId(),
                orderItemEntity.getProduct().getProductId()
        );
    }

    // Getters and Setters
    public int getProductId() {
        return productId;
    }

    public void setProductId(int productId) {
        this.productId = productId;
    }

    public int getQuantity() {
        return quantity;
    }

    public void setQuantity(int quantity) {
        this.quantity = quantity;
    }

    public int getVendorId() {
        return vendorId;
    }

    public void setVendorId(int vendorId) {
        this.vendorId = vendorId;
    }

    public int getVendorProductId() {
        return vendorProductId;
    }

    public void setVendorProductId(int vendorProductId) {
        this.vendorProductId = vendorProductId;
    }

    @Override
    public String toString() {
        return "SagaOrderItem{" +
                "productId='" + productId + '\'' +
                ", quantity=" + quantity +
                ", vendorId='" + vendorId + '\'' +
                ", vendorProductId='" + vendorProductId + '\'' +
                '}';
    }
}

