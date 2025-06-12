package com.oilerrig.backend.data.saga;

import java.io.Serializable;
import java.util.UUID;


public class SagaCompletedVendorOrder implements Serializable {
    private int vendorId;
    private UUID vendorOrderId;

    public SagaCompletedVendorOrder() {}

    public SagaCompletedVendorOrder(int vendorId, UUID vendorOrderId) {
        this.vendorId = vendorId;
        this.vendorOrderId = vendorOrderId;
    }

    // Getters and Setters
    public int getVendorId() {
        return vendorId;
    }

    public void setVendorId(int vendorId) {
        this.vendorId = vendorId;
    }

    public UUID getVendorOrderId() {
        return vendorOrderId;
    }

    public void setVendorOrderId(UUID vendorOrderId) {
        this.vendorOrderId = vendorOrderId;
    }

    @Override
    public String toString() {
        return "SagaCompletedVendorOrder{" +
                "vendorId='" + vendorId + '\'' +
                ", vendorOrderId='" + vendorOrderId + '\'' +
                '}';
    }
}
