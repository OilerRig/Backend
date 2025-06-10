package com.oilerrig.backend.gateway;

import com.oilerrig.backend.gateway.dto.VendorProductDto;
import com.oilerrig.backend.gateway.dto.VendorProductWithDetailsDto;

import java.util.List;
import java.util.Optional;

public interface VendorProductGateway {
    Optional<VendorProductDto> getProduct(Integer vendorId, Integer vendorProductId);
    Optional<VendorProductWithDetailsDto> getProductDetails(Integer vendorId, Integer vendorProductId);
    List<VendorProductDto> getAllProducts(Integer vendorId);
}