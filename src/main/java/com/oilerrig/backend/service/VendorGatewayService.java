package com.oilerrig.backend.service;

import com.oilerrig.backend.data.entity.VendorEntity;
import com.oilerrig.backend.data.repository.OrderRepository;
import com.oilerrig.backend.data.repository.ProductRepository;
import com.oilerrig.backend.data.repository.VendorProductRepository;
import com.oilerrig.backend.data.repository.VendorRepository;
import com.oilerrig.backend.gateway.VendorGateway;
import com.oilerrig.backend.gateway.VendorProductGateway;
import com.oilerrig.backend.gateway.impl.SpringVendorGateway;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.reactive.function.client.WebClient;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Service
public class VendorGatewayService {

    private static final Logger log = LoggerFactory.getLogger(VendorGatewayService.class);

    private final VendorRepository vendorRepository;
    private final Map<VendorEntity, VendorGateway> vendorGateways;

    @Autowired
    public VendorGatewayService(ProductRepository productRepository, VendorRepository vendorRepository, OrderRepository orderRepository) {
        this.vendorRepository = vendorRepository;
        this.vendorGateways = new HashMap<>();
    }

    public void updateVendors() {
        List<VendorEntity> vendors = vendorRepository.findAll();

        // add all new/missing vendors
        vendors.stream()
                .filter(v -> vendorGateways.keySet().stream().noneMatch(v::equals))
                .forEach(v -> vendorGateways.putIfAbsent(
                                v,
                                new SpringVendorGateway(WebClient.builder(), v.getBaseurl(), v.getApikey())
                        )
                );

        // remove all invalid/removed vendors
        vendorGateways.keySet()
                .stream()
                .filter(v -> !vendors.contains(v))
                .forEach(vendorGateways::remove);
    }

    public Map<VendorEntity, VendorGateway> getVendorGateways() {
        return vendorGateways;
    }
}
