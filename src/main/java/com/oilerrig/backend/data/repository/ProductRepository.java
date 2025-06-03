package com.oilerrig.backend.data.repository;

import com.oilerrig.backend.data.entity.ProductEntity;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ProductRepository extends JpaRepository<ProductEntity, Integer> {
}