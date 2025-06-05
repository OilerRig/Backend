package com.oilerrig.backend.data.saga;

import java.time.OffsetDateTime;
import java.util.List;

public class SagaMetadata {
    private List<SagaStep> steps;
    private String currentStep;
    private OffsetDateTime startedAt;
    private OffsetDateTime expiresAt;

    public List<SagaStep> getSteps() {
        return steps;
    }

    public void setSteps(List<SagaStep> steps) {
        this.steps = steps;
    }

    public String getCurrentStep() {
        return currentStep;
    }

    public void setCurrentStep(String currentStep) {
        this.currentStep = currentStep;
    }

    public OffsetDateTime getStartedAt() {
        return startedAt;
    }

    public void setStartedAt(OffsetDateTime startedAt) {
        this.startedAt = startedAt;
    }

    public OffsetDateTime getExpiresAt() {
        return expiresAt;
    }

    public void setExpiresAt(OffsetDateTime expiresAt) {
        this.expiresAt = expiresAt;
    }

    public static class SagaStep {
        private String name;
        private String status;
        private int attempts;
        private OffsetDateTime lastTriedAt;
        private String error;

        public String getName() {
            return name;
        }

        public void setName(String name) {
            this.name = name;
        }

        public String getStatus() {
            return status;
        }

        public void setStatus(String status) {
            this.status = status;
        }

        public int getAttempts() {
            return attempts;
        }

        public void setAttempts(int attempts) {
            this.attempts = attempts;
        }

        public OffsetDateTime getLastTriedAt() {
            return lastTriedAt;
        }

        public void setLastTriedAt(OffsetDateTime lastTriedAt) {
            this.lastTriedAt = lastTriedAt;
        }

        public String getError() {
            return error;
        }

        public void setError(String error) {
            this.error = error;
        }
    }
}
