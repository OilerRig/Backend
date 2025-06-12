package com.oilerrig.backend.data.saga;

import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import com.oilerrig.backend.data.entity.OrderEntity;
import com.oilerrig.backend.exception.ValidityException;

import java.io.Serializable;
import java.time.OffsetDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

public class SagaInstance implements Serializable {
    private UUID sagaId;
    private UUID brokerOrderId;
    private List<SagaOrderItem> orderItems;
    private OffsetDateTime createdAt;
    private OffsetDateTime expiresAt;
    private int attemptCount;
    private List<SagaCompletedVendorOrder> completedVendorOrders;

    public SagaInstance() {
        this.sagaId = UUID.randomUUID();
        this.createdAt = OffsetDateTime.now();
        this.expiresAt = OffsetDateTime.now().plusMinutes(15);
        this.attemptCount = 0;
        this.orderItems = new ArrayList<>();
        this.completedVendorOrders = new ArrayList<>();
    }

    public SagaInstance(OrderEntity orderEntity) {
        this();
        this.brokerOrderId = orderEntity.getId();
        if (orderEntity.getOrderItems() == null) throw new ValidityException("Invalid Saga: No Items");
        this.orderItems = orderEntity.getOrderItems().stream().map(SagaOrderItem::new).toList();
    }


    public UUID getSagaId() {
        return sagaId;
    }

    public void setSagaId(UUID sagaId) {
        this.sagaId = sagaId;
    }

    public UUID getBrokerOrderId() {
        return brokerOrderId;
    }

    public void setBrokerOrderId(UUID brokerOrderId) {
        this.brokerOrderId = brokerOrderId;
    }

    public List<SagaOrderItem> getOrderItems() {
        return orderItems;
    }

    public void setOrderItems(List<SagaOrderItem> orderItems) {
        this.orderItems = orderItems;
    }

    public OffsetDateTime getCreatedAt() {
        return createdAt;
    }

    public void setCreatedAt(OffsetDateTime createdAt) {
        this.createdAt = createdAt;
    }

    public OffsetDateTime getExpiresAt() {
        return expiresAt;
    }

    public void setExpiresAt(OffsetDateTime expiresAt) {
        this.expiresAt = expiresAt;
    }

    public int getAttemptCount() {
        return attemptCount;
    }

    public void setAttemptCount(int attemptCount) {
        this.attemptCount = attemptCount;
    }

    public List<SagaCompletedVendorOrder> getCompletedVendorOrders() {
        return completedVendorOrders;
    }

    public void setCompletedVendorOrders(List<SagaCompletedVendorOrder> completedVendorOrders) {
        this.completedVendorOrders = completedVendorOrders;
    }

    @Override
    public String toString() {
        return "SagaInstance{" +
                "sagaId=" + sagaId +
                ", brokerOrderId=" + brokerOrderId +
                ", orderItems=" + orderItems.size() + " items" +
                ", createdAt=" + createdAt +
                ", expiresAt=" + expiresAt +
                ", attemptCount=" + attemptCount +
                ", completedVendorOrders=" + completedVendorOrders.size() + " completed" +
                '}';
    }
}