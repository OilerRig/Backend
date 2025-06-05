package com.oilerrig.backend.domain;

import java.time.OffsetDateTime;
import java.util.List;


public class Order {
    private OffsetDateTime createdAt;
    private OrderStatus status;

    private User user;

    public enum OrderStatus {
        IN_PROGRESS, RETRYING, COMPLETED, CANCELLED
    }

    private List<OrderItem> orderItems;

    // business logic and helpers
    public boolean isEmpty() {
        return orderItems.isEmpty();
    }

    public boolean isCompleted() {
        return this.status == OrderStatus.COMPLETED;
    }

    public boolean isValidStateTransition(OrderStatus prev, OrderStatus next) {
        if (prev == null) return true; // allow setting if null (for init)
        if (next == null) return false; // dont allow setting to null
        if (prev == next) return false; // if no change, ignore

        return switch (prev) {
            case IN_PROGRESS -> true; // in progress can go to any state
            case RETRYING -> next == OrderStatus.COMPLETED || next == OrderStatus.CANCELLED; // retrying can be completed or cancelled
            case COMPLETED, CANCELLED -> false; // cant change state if completed or cancelled
        };
    }

    public boolean byGuest() {
        return this.user.isGuest();
    }

    public boolean isValid() {
        return this.user != null
                && this.orderItems != null
                && this.createdAt != null
                && this.status != null
                && this.user.isValid()
                && this.orderItems.stream().allMatch(OrderItem::isValid);

    }


    // getters and setters

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
        if (isValidStateTransition(this.status, status))
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