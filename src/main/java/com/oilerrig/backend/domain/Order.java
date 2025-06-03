package com.oilerrig.backend.domain;

import java.time.OffsetDateTime;
import java.util.List;


public class Order {
    private OffsetDateTime createdAt;
    private OrderStatus status;

    private User user;

    public enum OrderStatus {
        IN_PROGRESS, COMPLETED, CANCELLED
    }

    private List<OrderItem> orderItems;

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

    public void setOrderItems(List<OrderItem> orderItems) {
        this.orderItems = orderItems;
    }

    public List<OrderItem> getOrderItems() {
        return orderItems;
    }

    public User getUser() {
        return user;
    }

    public void setUser(User user) {
        this.user = user;
    }

}