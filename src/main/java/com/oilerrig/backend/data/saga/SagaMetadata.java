package com.oilerrig.backend.data.saga;

import java.util.List;

public class SagaMetadata {
    private List<SagaStep> steps;

    public List<SagaStep> getSteps() {
        return steps;
    }

    public void setSteps(List<SagaStep> steps) {
        this.steps = steps;
    }

    public static class SagaStep {
        private int productId;
        private int quantity;
        private SagaStepStatus status;

        public enum SagaStepStatus { PENDING, COMPLETED, FAILED }

        public int getProductId() {
            return productId;
        }

        public void setProductId(int productId) {
            this.productId = productId;
        }

        public int getQuantity() {
            return quantity;
        }

        public void setQuantity(int quantity) {
            this.quantity = quantity;
        }

        public SagaStepStatus getStatus() {
            return status;
        }

        public void setStatus(SagaStepStatus status) {
            this.status = status;
        }
    }
}
