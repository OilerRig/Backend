package com.oilerrig.backend.service;

import com.oilerrig.backend.data.dto.ProductDto;
import com.oilerrig.backend.data.repository.ProductRepository;
import com.oilerrig.backend.mapper.ProductMapper;
import jakarta.annotation.PostConstruct;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.web.PagedModel;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Map;

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

    public List<ProductDto> getAllProducts() {
        return productRepository.findAll().stream().map(productMapper::toDto).toList();
    }

    public PagedModel<ProductDto> getProducts(Pageable pageable) {
        return new PagedModel<>(
                productRepository.findAll(pageable).map(productMapper::toDto)
        );
    }

    public PagedModel<ProductDto> getProducts(Pageable pageable, String search) {
        return new PagedModel<>(
                productRepository.findAllByNameContaining(search, pageable).map(productMapper::toDto)
        );
    }

    // TODO define via vendor api
    // TODO possibly cache this data?
    public Map<String, String> getProductDetails(int productId) {
        return null;
    }


    // TODO SETUP DATABASE CACHES
    @PostConstruct
    public void initializeCaches() {

    }

    @Scheduled(fixedDelay = 1000)
    public void updateCaches() {

    }
    
}
