package com.oilerrig.backend.domain;

import com.oilerrig.backend.data.entity.OrderItemEntity;
import jakarta.persistence.*;
import org.hibernate.annotations.CreationTimestamp;

import java.time.OffsetDateTime;
import java.util.List;
import java.util.UUID;


public class Order {
    private UUID id;
    private String auth0_id;
    private Boolean isGuest;
    private OffsetDateTime createdAt;
    private OffsetDateTime resolvedAt;
    private OrderStatus status;

    public enum OrderStatus {
        PENDING, RETRYING, COMPLETED, CANCELLED
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
            case PENDING -> true; // pending can go to any state
            case RETRYING -> next == OrderStatus.COMPLETED || next == OrderStatus.CANCELLED; // retrying can be completed or cancelled
            case COMPLETED, CANCELLED -> false; // cant change state if completed or cancelled
        };
    }

    public boolean byGuest() {
        return this.isGuest;
    }

    public boolean isValid() {
        return  (this.isGuest == (this.auth0_id == null || this.auth0_id.isEmpty()))
                && this.orderItems != null
                && this.createdAt != null
                && this.resolvedAt != null
                && this.status != null
                && this.orderItems.stream().allMatch(OrderItem::isValid);

    }

    @Override
    public String toString() {
        return "Order{" +
                "id=" + id +
                ", auth0_id='" + auth0_id + '\'' +
                ", isGuest=" + isGuest +
                ", createdAt=" + createdAt +
                ", resolvedAt=" + resolvedAt +
                ", status=" + status +
                ", orderItems=" + orderItems +
                '}';
    }

    // getters and setters
    public UUID getId() {
        return id;
    }

    public void setId(UUID id) {
        this.id = id;
    }

    public String getAuth0_id() {
        return auth0_id;
    }

    public void setAuth0_id(String auth0_id) {
        this.auth0_id = auth0_id;
    }

    public Boolean getGuest() {
        return isGuest;
    }

    public void setGuest(Boolean guest) {
        isGuest = guest;
    }

    public OffsetDateTime getResolvedAt() {
        return resolvedAt;
    }

    public void setResolvedAt(OffsetDateTime resolvedAt) {
        this.resolvedAt = resolvedAt;
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
        if (isValidStateTransition(this.status, status))
           this.status = status;
    }

    public void setOrderItems(List<OrderItem> orderItems) {
        this.orderItems = orderItems;
    }

    public List<OrderItem> getOrderItems() {
        return orderItems;
    }

}