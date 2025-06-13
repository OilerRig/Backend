package com.oilerrig.backend.controller;

import com.oilerrig.backend.data.dto.OrderDto;
import com.oilerrig.backend.service.OrderService;
import com.oilerrig.backend.service.ProductService;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/admin")
public class AdminController {

    private final ProductService productService;
    private final OrderService orderService;

    public AdminController(ProductService productService, OrderService orderService) {
        this.productService = productService;
        this.orderService = orderService;
    }

    @GetMapping(value = "/caches/vendors")
    ResponseEntity<String> initVendors() {
        productService.updateVendors();
        return ResponseEntity.ok().body("Vendors Initialized Successfully");
    }

    @GetMapping(value = "/caches/reset")
    ResponseEntity<String> initCaches() {
        productService.resetCaches();
        return ResponseEntity.ok().body("Caches Initialized Successfully");
    }

    @GetMapping(value = "/caches/sync")
    ResponseEntity<String> syncCaches() {
        productService.updateCaches();
        return ResponseEntity.ok().body("Caches Synchronized Successfully");
    }

    @GetMapping(value = "/orders", produces = MediaType.APPLICATION_JSON_VALUE)
    ResponseEntity<List<OrderDto>> getAllOrders() {
        return ResponseEntity.ok().body(orderService.getAllOrders());
    }

}
