package com.oilerrig.backend.data.saga;

public class SagaStep {
    private final int productId;
    private final int quantity;
    private SagaStepStatus status;

    public SagaStep(int productId, int quantity, SagaStepStatus status) {
        this.productId = productId;
        this.quantity = quantity;
        this.status = status;
    }

    public SagaStep(int productId, int quantity) {
        this(productId, quantity, SagaStepStatus.PENDING);
    }

    public enum SagaStepStatus { PENDING, COMPLETED, FAILED }

    public int getProductId() {
        return productId;
    }

    public int getQuantity() {
        return quantity;
    }

    public SagaStepStatus getStatus() {
        return status;
    }

    public void setStatus(SagaStepStatus status) {
        this.status = status;
    }
}