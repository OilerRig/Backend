package com.oilerrig.backend.data.repository;

import aj.org.objectweb.asm.commons.Remapper;
import com.oilerrig.backend.data.entity.OrderItemEntity;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

public interface OrderItemRepository extends JpaRepository<OrderItemEntity, Integer> {
    Optional<OrderItemEntity> findByOrder_Id_AndProduct_Id(UUID id, int productId);

    Optional<OrderItemEntity> findByVendorOrderId(UUID vendorOrderId);

    List<OrderItemEntity> findAllByOrder_Id(UUID order_id);
}