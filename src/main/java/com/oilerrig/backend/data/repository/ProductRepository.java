package com.oilerrig.backend.data.repository;

import com.oilerrig.backend.data.entity.ProductEntity;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface ProductRepository extends JpaRepository<ProductEntity, Integer> {

    Page<ProductEntity> findAllByNameContaining(String name, Pageable pageable);

    List<ProductEntity> findAllByNameContaining(String name);
}