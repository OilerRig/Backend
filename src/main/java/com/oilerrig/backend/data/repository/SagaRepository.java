package com.oilerrig.backend.data.repository;

import com.oilerrig.backend.data.entity.SagaEntity;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.UUID;

public interface SagaRepository extends JpaRepository<SagaEntity, UUID> {
}