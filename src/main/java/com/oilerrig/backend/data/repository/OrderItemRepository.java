package com.oilerrig.backend.data.repository;

import com.oilerrig.backend.data.entity.OrderItemEntity;
import org.springframework.data.jpa.repository.JpaRepository;

public interface OrderItemRepository extends JpaRepository<OrderItemEntity, Integer> {
}