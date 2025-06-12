package com.oilerrig.backend.gateway;

import com.oilerrig.backend.gateway.dto.VendorCancelOrderDto;
import com.oilerrig.backend.gateway.dto.VendorOrderDto;
import com.oilerrig.backend.gateway.dto.VendorPlaceOrderDto;

import java.util.UUID;

public interface VendorOrderGateway {
    VendorOrderDto placeOrder(Integer vendorId, VendorPlaceOrderDto placeOrderDto);
    VendorOrderDto cancelOrder(Integer vendorId, VendorCancelOrderDto cancelOrderDto);
}