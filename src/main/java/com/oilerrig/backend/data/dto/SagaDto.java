package com.oilerrig.backend.data.dto;

import com.oilerrig.backend.data.entity.SagaEntity;
import com.oilerrig.backend.data.saga.SagaMetadata;

import java.io.Serializable;
import java.time.OffsetDateTime;
import java.util.Objects;
import java.util.UUID;

/**
 * DTO for {@link com.oilerrig.backend.data.entity.SagaEntity}
 */
public class SagaDto implements Serializable {
    private UUID id;
    private UUID orderId;
    private SagaEntity.SagaStatus status;
    private OffsetDateTime createdAt;
    private OffsetDateTime expiresAt;
    private int retryCount;
    private SagaMetadata metadata;

    public SagaDto() {
    }

    public SagaDto(UUID id, UUID orderId, SagaEntity.SagaStatus status, OffsetDateTime createdAt, OffsetDateTime expiresAt, int retryCount, SagaMetadata metadata) {
        this.id = id;
        this.orderId = orderId;
        this.status = status;
        this.createdAt = createdAt;
        this.expiresAt = expiresAt;
        this.retryCount = retryCount;
        this.metadata = metadata;
    }

    public UUID getId() {
        return id;
    }

    public SagaDto setId(UUID id) {
        this.id = id;
        return this;
    }

    public UUID getOrderId() {
        return orderId;
    }

    public SagaDto setOrderId(UUID orderId) {
        this.orderId = orderId;
        return this;
    }

    public SagaEntity.SagaStatus getStatus() {
        return status;
    }

    public SagaDto setStatus(SagaEntity.SagaStatus status) {
        this.status = status;
        return this;
    }

    public OffsetDateTime getCreatedAt() {
        return createdAt;
    }

    public SagaDto setCreatedAt(OffsetDateTime createdAt) {
        this.createdAt = createdAt;
        return this;
    }

    public OffsetDateTime getExpiresAt() {
        return expiresAt;
    }

    public SagaDto setExpiresAt(OffsetDateTime expiresAt) {
        this.expiresAt = expiresAt;
        return this;
    }

    public int getRetryCount() {
        return retryCount;
    }

    public SagaDto setRetryCount(int retryCount) {
        this.retryCount = retryCount;
        return this;
    }

    public SagaMetadata getMetadata() {
        return metadata;
    }

    public SagaDto setMetadata(SagaMetadata metadata) {
        this.metadata = metadata;
        return this;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        SagaDto entity = (SagaDto) o;
        return Objects.equals(this.id, entity.id) &&
                Objects.equals(this.orderId, entity.orderId) &&
                Objects.equals(this.status, entity.status) &&
                Objects.equals(this.createdAt, entity.createdAt) &&
                Objects.equals(this.expiresAt, entity.expiresAt) &&
                Objects.equals(this.retryCount, entity.retryCount) &&
                Objects.equals(this.metadata, entity.metadata);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, orderId, status, createdAt, expiresAt, retryCount, metadata);
    }

    @Override
    public String toString() {
        return getClass().getSimpleName() + "(" +
                "id = " + id + ", " +
                "orderId = " + orderId + ", " +
                "status = " + status + ", " +
                "createdAt = " + createdAt + ", " +
                "expiresAt = " + expiresAt + ", " +
                "retryCount = " + retryCount + ", " +
                "metadata = " + metadata + ")";
    }
}