package com.oilerrig.backend.service;

import com.oilerrig.backend.data.dto.ProductDto;
import com.oilerrig.backend.data.repository.ProductRepository;
import com.oilerrig.backend.mapper.ProductMapper;
import jakarta.annotation.PostConstruct;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class ProductService {

    private final ProductRepository productRepository;
    private final ProductMapper productMapper;

    @Autowired
    public ProductService(ProductRepository productRepository, ProductMapper productMapper) {
        this.productRepository = productRepository;
        this.productMapper = productMapper;
    }

    public ProductDto getProduct(int productId) {
        return null;
    }


    // TODO paginate
    public List<ProductDto> getAllProducts() {
        return productRepository.findAll().stream().map(productMapper::toDto).toList();
    }

    // TODO define via vendor api
    public void getProductDetails(int productId) {

    }

    @PostConstruct
    public void initializeCaches() {

    }

    @Scheduled(fixedDelay = 1000)
    public void updateCaches() {

    }
    
}
