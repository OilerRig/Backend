package com.oilerrig.backend.data.repository;

import com.oilerrig.backend.data.entity.VendorEntity;
import com.oilerrig.backend.exception.NotFoundException;
import com.oilerrig.backend.exception.VendorApiException;
import com.oilerrig.backend.gateway.VendorOrderGateway;
import com.oilerrig.backend.gateway.dto.VendorCancelOrderDto;
import com.oilerrig.backend.gateway.dto.VendorOrderDto;
import com.oilerrig.backend.gateway.dto.VendorPlaceOrderDto;
import com.oilerrig.backend.gateway.impl.SpringVendorGateway;
import com.oilerrig.backend.service.VendorGatewayService;
import io.github.resilience4j.circuitbreaker.annotation.CircuitBreaker;
import io.github.resilience4j.ratelimiter.annotation.RateLimiter;
import io.github.resilience4j.retry.annotation.Retry;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Repository;
import org.springframework.web.reactive.function.client.WebClient;
import org.springframework.web.reactive.function.client.WebClientResponseException;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.TimeUnit;
import java.util.stream.Collectors;

@Repository
public class VendorOrderRepository {

    private static final Logger log = LoggerFactory.getLogger(VendorOrderRepository.class);

    private final OrderRepository orderRepository;
    private final VendorGatewayService gatewayService;

    @Autowired
    public VendorOrderRepository(OrderRepository orderRepository, VendorGatewayService gatewayService) {
        this.orderRepository = orderRepository;
        this.gatewayService = gatewayService;
    }

    public VendorOrderDto placeOrder(int vendorId, VendorPlaceOrderDto command) throws VendorApiException {
        log.debug("VendorOrderRepository: Attempting to place order for product {} with vendor {}", command.getProductId(), vendorId);

         VendorOrderGateway gateway = gatewayService.getVendorGateways()
                 .entrySet()
                 .stream()
                 .filter(e -> e.getKey().getId() == vendorId).findFirst()
                 .orElseThrow(() -> new NotFoundException("Unknown Vendor: " + vendorId))
                 .getValue();
         return gateway.placeOrder(vendorId, command);
    }

    public VendorOrderDto cancelOrder(int vendorId, VendorCancelOrderDto command) throws VendorApiException {
        log.debug("VendorOrderRepository: Attempting to cancel order {} with vendor {}", command.getOrderId(), vendorId);
        VendorOrderGateway gateway = gatewayService.getVendorGateways()
                .entrySet()
                .stream()
                .filter(e -> e.getKey().getId() == vendorId).findFirst()
                .orElseThrow(() -> new NotFoundException("Unknown Vendor: " + vendorId))
                .getValue();

        return gateway.cancelOrder(vendorId, command);
    }
}
