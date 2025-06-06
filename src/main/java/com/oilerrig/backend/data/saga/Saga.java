package com.oilerrig.backend.data.saga;

import java.time.OffsetDateTime;
import java.util.List;

public class Saga {
    private OffsetDateTime createdAt;
    private OffsetDateTime expiresAt;
    private int currentStep;
    private List<SagaStep> steps;

    public Saga() {
        this(null);
    }

    public Saga(List<SagaStep> steps) {
        this.createdAt = OffsetDateTime.now();
        this.expiresAt = OffsetDateTime.now().plusMinutes(15);
        this.currentStep = 0;
        this.steps = steps;
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

    public int getCurrentStep() {
        return currentStep;
    }

    public void setCurrentStep(int currentStep) {
        this.currentStep = currentStep;
    }

    public List<SagaStep> getSteps() {
        return steps;
    }

    public void setSteps(List<SagaStep> steps) {
        this.steps = steps;
    }

}
