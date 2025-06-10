package com.oilerrig.backend.gateway;

import com.oilerrig.backend.gateway.dto.VendorCancelOrderDto;
import com.oilerrig.backend.gateway.dto.VendorOrderDto;
import com.oilerrig.backend.gateway.dto.VendorPlaceOrderDto;

import java.util.UUID;

public interface VendorOrderGateway {
    VendorOrderDto placeOrder(String vendorId, VendorPlaceOrderDto placeOrderDto);
    VendorOrderDto cancelOrder(String vendorId, VendorCancelOrderDto cancelOrderDto);
}