package com.oilerrig.backend.data.repository;

import com.oilerrig.backend.data.entity.OrderEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;
import java.util.UUID;

public interface OrderRepository extends JpaRepository<OrderEntity, UUID> {
    @Query("select o from OrderEntity o where o.auth0_id = ?1") // jpa went schizo, breaks without this
    List<OrderEntity> findAllByAuth0_id(String auth0_id);
}