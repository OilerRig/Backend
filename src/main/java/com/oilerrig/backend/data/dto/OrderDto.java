package com.oilerrig.backend.data.dto;

import java.io.Serializable;
import java.time.OffsetDateTime;
import java.util.List;
import java.util.Objects;
import java.util.UUID;

/**
 * DTO for {@link com.oilerrig.backend.data.entity.OrderEntity}
 */
public class OrderDto implements Serializable {
    private UUID id;
    private UUID userId;
    private String userRole;
    private OffsetDateTime createdAt;
    private String status;
    private List<OrderItemDto> orderItems;

    public OrderDto() {
    }

    public OrderDto(UUID id, UUID userId, String userRole, OffsetDateTime createdAt, String status, List<OrderItemDto> orderItems) {
        this.id = id;
        this.userId = userId;
        this.userRole = userRole;
        this.createdAt = createdAt;
        this.status = status;
        this.orderItems = orderItems;
    }

    public UUID getId() {
        return id;
    }

    public OrderDto setId(UUID id) {
        this.id = id;
        return this;
    }

    public UUID getUserId() {
        return userId;
    }

    public OrderDto setUserId(UUID userId) {
        this.userId = userId;
        return this;
    }

    public String getUserRole() {
        return userRole;
    }

    public OrderDto setUserRole(String userRole) {
        this.userRole = userRole;
        return this;
    }

    public OffsetDateTime getCreatedAt() {
        return createdAt;
    }

    public OrderDto setCreatedAt(OffsetDateTime createdAt) {
        this.createdAt = createdAt;
        return this;
    }

    public String getStatus() {
        return status;
    }

    public OrderDto setStatus(String status) {
        this.status = status;
        return this;
    }

    public List<OrderItemDto> getOrderItems() {
        return orderItems;
    }

    public OrderDto setOrderItems(List<OrderItemDto> orderItems) {
        this.orderItems = orderItems;
        return this;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        OrderDto entity = (OrderDto) o;
        return Objects.equals(this.id, entity.id) &&
                Objects.equals(this.userId, entity.userId) &&
                Objects.equals(this.userRole, entity.userRole) &&
                Objects.equals(this.createdAt, entity.createdAt) &&
                Objects.equals(this.status, entity.status) &&
                Objects.equals(this.orderItems, entity.orderItems);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, userId, userRole, createdAt, status, orderItems);
    }

    @Override
    public String toString() {
        return getClass().getSimpleName() + "(" +
                "id = " + id + ", " +
                "userId = " + userId + ", " +
                "userRole = " + userRole + ", " +
                "createdAt = " + createdAt + ", " +
                "status = " + status + ", " +
                "orderItems = " + orderItems + ")";
    }

    /**
     * DTO for {@link com.oilerrig.backend.data.entity.OrderItemEntity}
     */
    public static class OrderItemDto implements Serializable {
        private ProductDto product;
        private Integer quantity;

        public OrderItemDto() {
        }

        public OrderItemDto(ProductDto product, Integer quantity) {
            this.product = product;
            this.quantity = quantity;
        }

        public ProductDto getProduct() {
            return product;
        }

        public OrderItemDto setProduct(ProductDto product) {
            this.product = product;
            return this;
        }

        public Integer getQuantity() {
            return quantity;
        }

        public OrderItemDto setQuantity(Integer quantity) {
            this.quantity = quantity;
            return this;
        }

        @Override
        public boolean equals(Object o) {
            if (this == o) return true;
            if (o == null || getClass() != o.getClass()) return false;
            OrderItemDto entity = (OrderItemDto) o;
            return Objects.equals(this.product, entity.product) &&
                    Objects.equals(this.quantity, entity.quantity);
        }

        @Override
        public int hashCode() {
            return Objects.hash(product, quantity);
        }

        @Override
        public String toString() {
            return getClass().getSimpleName() + "(" +
                    "product = " + product + ", " +
                    "quantity = " + quantity + ")";
        }
    }
}