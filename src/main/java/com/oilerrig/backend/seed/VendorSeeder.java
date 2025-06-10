package com.oilerrig.backend.seed;

import com.github.javafaker.Faker;
import com.oilerrig.backend.data.entity.*;
import com.oilerrig.backend.data.repository.*;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

@Component
@ConditionalOnProperty(
        name = "app.seed.type",
        havingValue = "vendors",
        matchIfMissing = false
)
public class VendorSeeder implements CommandLineRunner {
    private static final Logger log = LoggerFactory.getLogger(VendorSeeder.class);

    private final UserRepository userRepository;
    private final VendorRepository vendorRepository;
    private final ProductRepository productRepository;
    private final OrderRepository orderRepository;
    private final OrderItemRepository orderItemRepository;

    public VendorSeeder(UserRepository userRepository,
                          VendorRepository vendorRepository,
                          ProductRepository productRepository,
                          OrderRepository orderRepository,
                          OrderItemRepository orderItemRepository) {
        this.userRepository = userRepository;
        this.vendorRepository = vendorRepository;
        this.productRepository = productRepository;
        this.orderRepository = orderRepository;
        this.orderItemRepository = orderItemRepository;
    }


    @Override
    @Transactional
    public void run(String... args) throws Exception {
        // Clear existing data
        orderRepository.deleteAll();
        orderItemRepository.deleteAll();
        productRepository.deleteAll();
        vendorRepository.deleteAll();
        userRepository.deleteAll();


        // Seed Vendors
        List<VendorEntity> vendors = new ArrayList<>();

        // spring vendor 1
        VendorEntity vendor = new VendorEntity();
        vendor.setName("LOCAL SPRING VENDOR");
        vendor.setBaseurl("http://localhost:8081");
        vendor.setApikey("no key yet");
        vendors.add(vendor);

        vendorRepository.saveAll(vendors);

        log.info("Database seeded successfully with vendor data.");
    }
}