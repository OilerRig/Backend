package com.oilerrig.backend.controller;

import com.oilerrig.backend.data.dto.ProductDto;
import com.oilerrig.backend.service.ProductService;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.web.PagedModel;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("/products")
class ProductController {

    private final ProductService productService;

    ProductController(ProductService productService) {
        this.productService = productService;
    }

    @GetMapping(value = "", produces = MediaType.APPLICATION_JSON_VALUE)
    ResponseEntity<?> getProductPage(
            Pageable pageable,
            @RequestParam(value = "search") Optional<String> search
    ) {
        ResponseEntity<?> response;

        if (!pageable.isPaged()) {
            response = ResponseEntity.ok().body(
                    productService.getAllProducts()
            );
        }
        else if (search.isEmpty()) {
            response = ResponseEntity.ok().body(
                    productService.getProducts(pageable)
            );
        }
        else {
            response = ResponseEntity.ok().body(
                    productService.getProducts(pageable, search.get())
            );
        }

        return response;
    }

    @GetMapping(value = "/{id}", produces = MediaType.APPLICATION_JSON_VALUE)
    ResponseEntity<ProductDto> getProduct(@PathVariable int id) {
        return ResponseEntity.ok().body(productService.getProduct(id));
    }

    @GetMapping(value = "/{id}/details", produces = MediaType.APPLICATION_JSON_VALUE)
    ResponseEntity<ProductDto> getProductDetails(@PathVariable int id) {
        return ResponseEntity.ok().body(productService.getProductDetails(id));
    }
}
