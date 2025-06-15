package com.oilerrig.backend.service;

import com.oilerrig.backend.data.dto.ProductDto;
import com.oilerrig.backend.data.entity.ProductEntity;
import com.oilerrig.backend.data.repository.ProductRepository;
import com.oilerrig.backend.data.repository.VendorProductRepository;
import com.oilerrig.backend.exception.NotFoundException;
import com.oilerrig.backend.mapper.ProductMapper;
import jakarta.annotation.PostConstruct;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Pageable;
import org.springframework.data.web.PagedModel;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.concurrent.TimeUnit;

@Service
public class ProductService {

    private final ProductRepository productRepository;
    private final ProductMapper productMapper;
    private final VendorProductRepository vendorProductRepository;
    private final VendorGatewayService vendorGatewayService;

    @Autowired
    public ProductService(ProductRepository productRepository, ProductMapper productMapper, VendorProductRepository vendorProductRepository, VendorGatewayService vendorGatewayService) {
        this.productRepository = productRepository;
        this.productMapper = productMapper;
        this.vendorProductRepository = vendorProductRepository;
        this.vendorGatewayService = vendorGatewayService;
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

    public ProductDto getProductDetails(int productId) {
        ProductEntity product = productRepository
                .findById(productId)
                .orElseThrow(
                        () -> new NotFoundException("Product with id: " + productId + " not found in cache")
                );

        return vendorProductRepository
                    .getProductDetails(product.getVendor().getId(), product.getProductId())
                    .orElseThrow(
                            () -> new NotFoundException("Product with id: " + productId + " not found at vendor")
                    );
    }

    public void resetCaches() {
        this.updateVendors();
        vendorProductRepository.restartAndSyncProductCache();
    }

    @Scheduled(fixedRate = 5*60, initialDelay = 10, timeUnit = TimeUnit.SECONDS)
    public void updateCaches() {
        this.updateVendors();
        vendorProductRepository.synchronizeStaleProducts();
    }

    public void updateVendors() {
        vendorGatewayService.updateVendors();
    }
    
}
