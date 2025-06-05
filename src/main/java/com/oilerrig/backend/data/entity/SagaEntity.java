package com.oilerrig.backend.data.entity;

import com.oilerrig.backend.data.saga.SagaMetadata;
import com.vladmihalcea.hibernate.type.json.JsonType;
import jakarta.persistence.*;
import org.hibernate.annotations.Type;

import java.time.OffsetDateTime;
import java.util.UUID;

@Entity
@Table(name = "order_sagas", schema = "Broker")
public class SagaEntity {

    @Id
    @GeneratedValue
    private UUID id;

    @Column(nullable = false)
    private UUID orderId;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private SagaStatus status;

    @Column(nullable = false)
    private OffsetDateTime createdAt;

    @Column(nullable = false)
    private OffsetDateTime expiresAt;

    @Column(nullable = false)
    private int retryCount;

    @Column
    private String lastFailedStep;

    @Column
    @Type(JsonType.class)
    private SagaMetadata metadata;

    public enum SagaStatus {
        PENDING,
        COMPLETED,
        FAILED,
        CANCELLING,
        CANCELLED
    }

    public UUID getId() {
        return id;
    }

    public SagaEntity setId(UUID id) {
        this.id = id;
        return this;
    }

    public UUID getOrderId() {
        return orderId;
    }

    public SagaEntity setOrderId(UUID orderId) {
        this.orderId = orderId;
        return this;
    }

    public SagaStatus getStatus() {
        return status;
    }

    public SagaEntity setStatus(SagaStatus status) {
        this.status = status;
        return this;
    }

    public OffsetDateTime getCreatedAt() {
        return createdAt;
    }

    public SagaEntity setCreatedAt(OffsetDateTime createdAt) {
        this.createdAt = createdAt;
        return this;
    }

    public OffsetDateTime getExpiresAt() {
        return expiresAt;
    }

    public SagaEntity setExpiresAt(OffsetDateTime expiresAt) {
        this.expiresAt = expiresAt;
        return this;
    }

    public int getRetryCount() {
        return retryCount;
    }

    public SagaEntity setRetryCount(int retryCount) {
        this.retryCount = retryCount;
        return this;
    }

    public String getLastFailedStep() {
        return lastFailedStep;
    }

    public SagaEntity setLastFailedStep(String lastFailedStep) {
        this.lastFailedStep = lastFailedStep;
        return this;
    }

    public SagaMetadata getMetadata() {
        return metadata;
    }

    public SagaEntity setMetadata(SagaMetadata metadata) {
        this.metadata = metadata;
        return this;
    }
}
