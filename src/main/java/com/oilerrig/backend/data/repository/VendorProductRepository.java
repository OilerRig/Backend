package com.oilerrig.backend.data.repository;

import com.oilerrig.backend.data.dto.ProductDto;
import com.oilerrig.backend.data.entity.ProductEntity;
import com.oilerrig.backend.data.entity.VendorEntity;
import com.oilerrig.backend.exception.NotFoundException;
import com.oilerrig.backend.exception.VendorApiException;
import com.oilerrig.backend.gateway.dto.VendorProductDto;
import com.oilerrig.backend.gateway.dto.VendorProductWithDetailsDto;
import com.oilerrig.backend.service.VendorGatewayService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import java.time.OffsetDateTime;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.List;
import java.util.Optional;

@Repository
public class VendorProductRepository {

    private static final Logger log = LoggerFactory.getLogger(VendorProductRepository.class);

    private final ProductRepository productRepository;
    private final VendorGatewayService gatewayService;
    private final OrderRepository orderRepository;

    @Autowired
    public VendorProductRepository(ProductRepository productRepository, VendorGatewayService gatewayService, OrderRepository orderRepository) {
        this.productRepository = productRepository;
        this.gatewayService = gatewayService;
        this.orderRepository = orderRepository;
    }


    public Optional<ProductEntity> getProduct(int vendorId, int productId) {
        Optional<ProductEntity> cachedProduct = productRepository.findById(productId);
        if (cachedProduct.isPresent() && !productRepository.isStale(cachedProduct.get())) {
            return cachedProduct;
        }

        VendorEntity vendor = gatewayService.getVendorGateways()
                .keySet()
                .stream()
                .filter(e -> e.getId() == vendorId)
                .findFirst()
                .orElseThrow(() -> new NotFoundException("Vendor with id " + vendorId + " not found"));

        Optional<VendorProductDto> vendorProductDetails =
                gatewayService.getVendorGateways().get(vendor)
                        .getProduct(vendorId, productId);

        return vendorProductDetails.map(p ->
                mapToProductEntity(p, vendor)
        );
    }

    public Optional<ProductDto> getProductDetails(int vendorId, int productId) throws VendorApiException {
        VendorEntity vendor = gatewayService.getVendorGateways()
                .keySet()
                .stream()
                .filter(e -> e.getId() == vendorId)
                .findFirst()
                .orElseThrow(() -> new NotFoundException("Vendor with id " + vendorId + " not found"));

        VendorProductWithDetailsDto vendorProductDetails =
                gatewayService.getVendorGateways().get(vendor)
                        .getProductDetails(vendorId, productId)
                        .orElseThrow(() -> new NotFoundException("Product with id " + productId + " not found"));

        updateProductCache(vendor, vendorProductDetails, true);

        return Optional.of(mapToProductDto(vendorProductDetails));
    }

    @Transactional
    public void restartAndSyncProductCache() throws VendorApiException {
        orderRepository.deleteAll();
        productRepository.deleteAllAndCascade();

        log.info("Resetting and Synchronizing all products for {} vendors", gatewayService.getVendorGateways().size());
        for (var entry : gatewayService.getVendorGateways().entrySet()) {
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
        for (var entry : gatewayService.getVendorGateways().entrySet()) {
            List<VendorProductDto> vendorProducts = entry.getValue().getAllProducts(entry.getKey().getId());

            for (VendorProductDto vendorProductDto : vendorProducts) {
                updateProductCache(entry.getKey(), vendorProductDto, false);
            }
        }
        productRepository.removeStaleProducts(); // to remove products that arent there anymore
    }

    private void updateProductCache(VendorEntity vendor, VendorProductDto dto, boolean force) {
        int vendorId = vendor.getId();
        int productId = dto.getId();

        Optional<ProductEntity> entity = productRepository.findByVendor_IdAndProductId(vendorId, productId);
        if (entity.isEmpty()) { // if empty create it
            ProductEntity productEntity = mapToProductEntity(dto, vendor);
            productEntity.setLastUpdated(OffsetDateTime.now());
            productRepository.save(productEntity);
        }
        else if (force || productRepository.isStale(entity.get())) { // if stale update it, force overrides stale check
            ProductEntity productEntity = entity.get();
            productEntity.setLastUpdated(OffsetDateTime.now());
            productEntity.setStock(dto.getStock());
            productEntity.setPrice(dto.getPrice());
            productEntity.setName(dto.getName());
            productRepository.save(productEntity);
        }
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