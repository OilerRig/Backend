package com.oilerrig.backend.data.repository;

import com.oilerrig.backend.data.entity.VendorEntity;
import org.springframework.data.jpa.repository.JpaRepository;

public interface VendorRepository extends JpaRepository<VendorEntity, Integer> {
}