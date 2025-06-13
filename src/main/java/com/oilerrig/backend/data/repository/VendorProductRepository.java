package com.oilerrig.backend.data.repository;

import com.oilerrig.backend.data.dto.ProductDto;
import com.oilerrig.backend.data.entity.ProductEntity;
import com.oilerrig.backend.data.entity.VendorEntity;
import com.oilerrig.backend.exception.NotFoundException;
import com.oilerrig.backend.exception.VendorApiException;
import com.oilerrig.backend.gateway.VendorProductGateway;
import com.oilerrig.backend.gateway.dto.VendorProductDto;
import com.oilerrig.backend.gateway.dto.VendorProductWithDetailsDto;
import com.oilerrig.backend.gateway.impl.SpringVendorGateway;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.reactive.function.client.WebClient;

import java.time.OffsetDateTime;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.concurrent.TimeUnit;
import java.util.stream.Collectors;

@Repository
public class VendorProductRepository {

    private static final Logger log = LoggerFactory.getLogger(VendorProductRepository.class);

    private final ProductRepository productRepository;
    private final VendorRepository vendorRepository;
    private final Map<VendorEntity, VendorProductGateway> vendorGateways;
    private final OrderRepository orderRepository;

    @Autowired
    public VendorProductRepository(ProductRepository productRepository, VendorRepository vendorRepository, OrderRepository orderRepository) {
        this.productRepository = productRepository;
        this.vendorRepository = vendorRepository;
        this.vendorGateways = new HashMap<>();
        this.orderRepository = orderRepository;
    }

    public void updateVendors() {
        vendorGateways.putAll(vendorRepository.findAll()
                .stream()
                .collect(Collectors.toMap(
                        v -> v,
                        v -> new SpringVendorGateway(WebClient.builder(), v.getBaseurl(), v.getApikey())
                )));
    }


    public Optional<ProductEntity> getProduct(int vendorId, int productId) {
        Optional<ProductEntity> cachedProduct = productRepository.findById(productId);
        if (cachedProduct.isPresent() && !productRepository.isStale(cachedProduct.get())) {
            return cachedProduct;
        }

        VendorEntity vendor = vendorGateways
                .keySet()
                .stream()
                .filter(e -> e.getId() == vendorId)
                .findFirst()
                .orElseThrow(() -> new NotFoundException("Vendor with id " + vendorId + " not found"));

        Optional<VendorProductDto> vendorProductDetails =
                vendorGateways.get(vendor)
                        .getProduct(vendorId, productId);

        return vendorProductDetails.map(p ->
                mapToProductEntity(p, vendor)
        );
    }

    public Optional<ProductDto> getProductDetails(int vendorId, int productId) throws VendorApiException {
        VendorEntity vendor = vendorGateways
                .keySet()
                .stream()
                .filter(e -> e.getId() == vendorId)
                .findFirst()
                .orElseThrow(() -> new NotFoundException("Vendor with id " + vendorId + " not found"));

        Optional<VendorProductWithDetailsDto> vendorProductDetails =
                vendorGateways.get(vendor)
                        .getProductDetails(vendorId, productId);

        return vendorProductDetails.map(this::mapToProductDto);
    }



    @Transactional
    public void synchronizeAllProducts() throws VendorApiException {
        orderRepository.deleteAll();
        productRepository.deleteAllAndResetIdentity();
        log.info("Synchronizing all products for " + vendorGateways.size() + " vendors");
        for (var entry : vendorGateways.entrySet()) {
            List<VendorProductDto> vendorProducts = entry.getValue().getAllProducts(entry.getKey().getId());

            vendorProducts.stream()
                    .map(p -> mapToProductEntity(p, entry.getKey()))
                    .peek(p -> p.setLastUpdated(OffsetDateTime.now()))
                    .forEach(productRepository::save);

        }
    }

    @Transactional
    public void synchronizeStaleProducts() throws VendorApiException {
        log.info("Synchronizing stale products");
        for (var entry : vendorGateways.entrySet()) {
            List<VendorProductDto> vendorProducts = entry.getValue().getAllProducts(entry.getKey().getId());

            for (VendorProductDto vendorProductDto : vendorProducts) {
                Optional<ProductEntity> entity = productRepository.findByVendor_IdAndProductId(entry.getKey().getId(), vendorProductDto.getId());
                if (entity.isEmpty()) { // if empty create it
                    ProductEntity productEntity = mapToProductEntity(vendorProductDto, entry.getKey());
                    productEntity.setLastUpdated(OffsetDateTime.now());
                    productRepository.save(productEntity);
                }
                else if (productRepository.isStale(entity.get())) { // if stale update it
                    ProductEntity productEntity = entity.get();
                    productEntity.setLastUpdated(OffsetDateTime.now());
                    productEntity.setStock(vendorProductDto.getStock());
                    productEntity.setPrice(vendorProductDto.getPrice());
                    productEntity.setName(vendorProductDto.getName());
                    productRepository.save(productEntity);
                }
            }
        }
        productRepository.removeStaleProducts(); // to remove products that arent there anymore
    }

    private ProductEntity mapToProductEntity(VendorProductDto vendorDto, VendorEntity vendor) {
        ProductEntity product = new ProductEntity();
        product.setProductId(vendorDto.getId());
        product.setName(vendorDto.getName());
        product.setPrice(vendorDto.getPrice());
        product.setStock(vendorDto.getStock());
        product.setVendor(vendor);
        return product;
    }

    private ProductDto mapToProductDto(VendorProductWithDetailsDto vendorDto) {
        ProductDto product = new ProductDto();
        product.setId(vendorDto.getId());
        product.setName(vendorDto.getName());
        product.setPrice(vendorDto.getPrice());
        product.setStock(vendorDto.getStock());
        product.setDetails(vendorDto.getDetails());
        return product;
    }
}