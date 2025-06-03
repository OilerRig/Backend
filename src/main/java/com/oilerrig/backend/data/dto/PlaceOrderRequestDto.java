package com.oilerrig.backend.data.dto;

import java.io.Serializable;
import java.util.List;
import java.util.Objects;
import java.util.UUID;

/**
 * DTO for {@link com.oilerrig.backend.data.entity.OrderEntity}
 */
public class PlaceOrderRequestDto implements Serializable {
    private List<Integer> orderItemProductIds;
    private List<Integer> orderItemQuantities;

    public PlaceOrderRequestDto() {
    }

    public PlaceOrderRequestDto(UUID userId, List<Integer> orderItemProductIds, List<Integer> orderItemQuantities) {
        this.orderItemProductIds = orderItemProductIds;
        this.orderItemQuantities = orderItemQuantities;
    }

    public List<Integer> getOrderItemProductIds() {
        return orderItemProductIds;
    }

    public PlaceOrderRequestDto setOrderItemProductIds(List<Integer> orderItemProductIds) {
        this.orderItemProductIds = orderItemProductIds;
        return this;
    }

    public List<Integer> getOrderItemQuantities() {
        return orderItemQuantities;
    }

    public PlaceOrderRequestDto setOrderItemQuantities(List<Integer> orderItemQuantities) {
        this.orderItemQuantities = orderItemQuantities;
        return this;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        PlaceOrderRequestDto entity = (PlaceOrderRequestDto) o;
        return Objects.equals(this.orderItemProductIds, entity.orderItemProductIds) &&
                Objects.equals(this.orderItemQuantities, entity.orderItemQuantities);
    }

    @Override
    public int hashCode() {
        return Objects.hash(orderItemProductIds, orderItemQuantities);
    }

    @Override
    public String toString() {
        return getClass().getSimpleName() + "(" +
                "orderItemProducts = " + orderItemProductIds + ", " +
                "orderItemQuantities = " + orderItemQuantities + ")";
    }
}