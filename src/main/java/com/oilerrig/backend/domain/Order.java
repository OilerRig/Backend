package com.oilerrig.backend.domain;

import java.time.OffsetDateTime;
import java.util.List;
import java.util.UUID;



public class Order {
    private UUID id;
    private UUID userId;
    private OffsetDateTime createdAt;
    private OrderStatus status;

    public enum OrderStatus {
        IN_PROGRESS, COMPLETED, CANCELLED
    }

    private List<OrderItem> orderItems;

    public UUID getId() {
        return id;
    }

    public void setId(UUID id) {
        this.id = id;
    }

    public UUID getUserId() {
        return userId;
    }

    public void setUserId(UUID userId) {
        this.userId = userId;
    }

    public OffsetDateTime getCreatedAt() {
        return createdAt;
    }

    public void setCreatedAt(OffsetDateTime createdAt) {
        this.createdAt = createdAt;
    }

    public OrderStatus getStatus() {
        return status;
    }

    public void setStatus(OrderStatus status) {
        this.status = status;
    }

    public List<OrderItem> getOrderItems() {
        return orderItems;
    }
}