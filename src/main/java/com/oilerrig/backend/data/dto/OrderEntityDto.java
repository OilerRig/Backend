package com.oilerrig.backend.data.dto;

import java.io.Serializable;
import java.time.OffsetDateTime;
import java.util.List;
import java.util.Objects;
import java.util.UUID;

/**
 * DTO for {@link com.oilerrig.backend.data.entity.OrderEntity}
 */
public class OrderEntityDto implements Serializable {
    private UUID id;
    private UUID userId;
    private String userRole;
    private OffsetDateTime createdAt;
    private String status;
    private List<OrderItemEntityDto> orderItems;

    public OrderEntityDto() {
    }

    public OrderEntityDto(UUID id, UUID userId, String userRole, OffsetDateTime createdAt, String status, List<OrderItemEntityDto> orderItems) {
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

    public OrderEntityDto setId(UUID id) {
        this.id = id;
        return this;
    }

    public UUID getUserId() {
        return userId;
    }

    public OrderEntityDto setUserId(UUID userId) {
        this.userId = userId;
        return this;
    }

    public String getUserRole() {
        return userRole;
    }

    public OrderEntityDto setUserRole(String userRole) {
        this.userRole = userRole;
        return this;
    }

    public OffsetDateTime getCreatedAt() {
        return createdAt;
    }

    public OrderEntityDto setCreatedAt(OffsetDateTime createdAt) {
        this.createdAt = createdAt;
        return this;
    }

    public String getStatus() {
        return status;
    }

    public OrderEntityDto setStatus(String status) {
        this.status = status;
        return this;
    }

    public List<OrderItemEntityDto> getOrderItems() {
        return orderItems;
    }

    public OrderEntityDto setOrderItems(List<OrderItemEntityDto> orderItems) {
        this.orderItems = orderItems;
        return this;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        OrderEntityDto entity = (OrderEntityDto) o;
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
    public static class OrderItemEntityDto implements Serializable {
        private ProductEntityDto product;
        private Integer quantity;

        public OrderItemEntityDto() {
        }

        public OrderItemEntityDto(ProductEntityDto product, Integer quantity) {
            this.product = product;
            this.quantity = quantity;
        }

        public ProductEntityDto getProduct() {
            return product;
        }

        public OrderItemEntityDto setProduct(ProductEntityDto product) {
            this.product = product;
            return this;
        }

        public Integer getQuantity() {
            return quantity;
        }

        public OrderItemEntityDto setQuantity(Integer quantity) {
            this.quantity = quantity;
            return this;
        }

        @Override
        public boolean equals(Object o) {
            if (this == o) return true;
            if (o == null || getClass() != o.getClass()) return false;
            OrderItemEntityDto entity = (OrderItemEntityDto) o;
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