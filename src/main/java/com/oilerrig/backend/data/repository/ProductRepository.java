package com.oilerrig.backend.data.repository;

import com.oilerrig.backend.data.entity.ProductEntity;
import jakarta.transaction.Transactional;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.NativeQuery;
import org.springframework.data.jpa.repository.Query;

import java.time.Duration;
import java.time.OffsetDateTime;
import java.util.List;
import java.util.Optional;

public interface ProductRepository extends JpaRepository<ProductEntity, Integer> {

    Page<ProductEntity> findAllByNameContaining(String name, Pageable pageable);

    List<ProductEntity> findAllByNameContaining(String name);

    @NativeQuery("TRUNCATE TABLE tableName RESTART IDENTITY")
    void deleteAllAndResetIdentity();

    default boolean isStale(ProductEntity productEntity) {
        return Duration.between(productEntity.getLastUpdated(), OffsetDateTime.now()).abs().toMinutes() > 5;
    }

    @Query("select p from ProductEntity p")
    @Transactional
    default void removeStaleProducts() {
        findAll().stream().filter(this::isStale).forEach(this::delete);
    }

    Optional<ProductEntity> findByVendor_IdAndProductId(Integer vendorId, Integer productId);
}