package com.oilerrig.backend.controller;

import com.oilerrig.backend.data.dto.OrderDto;
import com.oilerrig.backend.data.dto.ProductDto;
import com.oilerrig.backend.service.ProductService;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;
import java.util.UUID;

@RestController
class ProductController {

    private final ProductService productService;

    ProductController(ProductService productService) {
        this.productService = productService;
    }

    @GetMapping(value = "/products", produces = MediaType.APPLICATION_JSON_VALUE)
    ResponseEntity<List<ProductDto>> getAllProducts() {
        return ResponseEntity.ok().body(productService.getAllProducts());
    }

    @GetMapping(value = "/products/{id}", produces = MediaType.APPLICATION_JSON_VALUE)
    ResponseEntity<ProductDto> getProduct(@PathVariable int id) {
        return ResponseEntity.ok().body(productService.getProduct(id));
    }

    @GetMapping(value = "/products/{id}/details", produces = MediaType.APPLICATION_JSON_VALUE)
    ResponseEntity<?> getProductDetails(@PathVariable int id) {
        return ResponseEntity.ok().body(productService.getProduct(id));
    }

}
