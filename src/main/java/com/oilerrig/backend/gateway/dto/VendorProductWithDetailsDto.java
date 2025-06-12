package com.oilerrig.backend.gateway.dto;

import java.util.Map;
import java.util.UUID;

public class VendorProductWithDetailsDto extends VendorProductDto {
    private Map<String, String> details;

    public Map<String, String> getDetails() {
        return details;
    }
    public void setDetails(Map<String, String> details) {
        this.details = details;
    }

    @Override
    public String toString() {
        return "VendorProductWithDetailsDto{" +
                "details=" + details +
                '}';
    }
}
